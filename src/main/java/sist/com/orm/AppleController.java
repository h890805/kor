package sist.com.orm;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import dao.MemberDao;
import model.MemberBean;


 
@Controller
public class AppleController {
	@Autowired
	private MemberDao dao;
	
	@Resource
	private MemberBean bean;
	
 	
	@RequestMapping(value="loginPro.do")
	public String loginProcess(String userId, String userpassword,HttpSession session) {//아이디 비밀번호가 정보와 일치하는지 확인
			session.setAttribute("userId", userId);
			return dao.memberIdCheck(userId,userpassword)?"redirect:loginCheck.do":
				"redirect:SIST/sub/login.jsp";
	} 
 
	@RequestMapping(value="loginCheck.do")
	public String loginChecksession(String userId,HttpSession session,Model model) {//레벨 확인 후 관리자,회원 구분
	 
		MemberBean bean = dao.findlevel((String) session.getAttribute("userId"));
		if(Integer.parseInt(bean.getMlevel())==0) {
			 return "/SIST/adminIndex";
			 
		}else if(Integer.parseInt(bean.getMlevel())==1) {
			 return "/SIST/index";
			 
		}
		return null;			
	}
	 
	 @RequestMapping(value="logoutPro.do")
	 public String logoutPro(HttpServletRequest request) {//로그아웃 = 로그인 후 생성된 세션 제거
		
		 HttpSession session = request.getSession();
		 session.invalidate();
		 return "/SIST/index";
	 }
	
	@RequestMapping(value="indexlogin.do")
	public String login() {
		return "redirect:/SIST/sub/login.jsp";
	}
	
	@RequestMapping(value="joinRentcar.do")
	public String joinmember() {
		
		return "redirect:/SIST/sub/joinRentcar.jsp";
	}
	
	
	@RequestMapping(value="memberInsert.do")
	public String insertMember(MemberBean bean) {
	 
		bean.setMlevel("1");
		dao.insertMember(bean);
		
		return "memberSuccess";
	}
	
	@RequestMapping(value="searchAdress.do")
	public String serarchAdress(String dong,Model model) {
		/* List<ZipBean>list=dong!=null?MemberDao.selectZipCode(dong):null;*/
		//dao.selectZipCode(dong);
 	model.addAttribute("dongList", dao.selectZipCode(dong)); 
		return "SIST/sub/address";
	}
	

	
/*	
	*	@RequestMapping(value="loginCheck.do")
	@ResponseBody
	public Boolean ProductListPro(HttpServletRequest request, HttpServletResponse response, Model model,HttpSession session)throws Exception {
		model.addAttribute("adminList",dao.selectmember((String) session.getAttribute("userId")));
		
		HttpSession session1 = request.getSession();
		if(session1 == null) {
			response.sendRedirect(request.getContextPath()+"/SIST/sub/login.jsp");
			return false;
		}
		
		MemberBean bean = (MemberBean) session1.getAttribute("/SIST/index.jsp");
		
		if(bean == null) {
			response.sendRedirect(request.getContextPath()+"/SIST/sub/login.jsp");
			return false;
		}
		return true;
		//return "SIST/index.jsp";
}*/ 

	

}








