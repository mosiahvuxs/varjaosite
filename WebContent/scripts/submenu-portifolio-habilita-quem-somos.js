// JavaScript Document

$(document).ready(function(){
		$('.item-menu-quem-somos').mouseenter(function(){
			$('.submenu-quem-somos').show();
			$('.sombra-wrapper-submenu').show();
			$('.submenu-atuacao').hide();
			$('.submenu-clientes').hide();
			$('.submenu-portifolio').hide();
			$('.submenu-portifolio').hide();
			$('.item-menu-quem-somos a').css({
				'background-color': '#0c5d9e',
				'padding': '10px 4px',
				'color': 'white',
				'margin': '0px 0px 0px -8px',
				'border-radius': '10px 10px 0px 0px' 
				});
			$('.item-menu-portifolio a').css({
				'background-color': 'transparent',
				'color': '#636466',
				});
			$('.item-menu-clientes a').css({
				'background-color': 'transparent',
				'color': '#636466',
				});
			});			
		$('.submenu-quem-somos').mouseenter(function(){
			$('.submenu-quem-somos').show();
			$('.sombra-wrapper-submenu').show();
			$('.submenu-atuacao').hide();
			$('.submenu-clientes').hide();
			$('.submenu-portifolio').hide();
			$('.submenu-portifolio').hide();
			$('.item-menu-quem-somos a').css({
				'background-color': '#0c5d9e',
				'padding': '10px 4px',
				'color': 'white',
				'margin': '0px 0px 0px -8px',
				'border-radius': '10px 10px 0px 0px' 
				});
			$('.item-menu-portifolio a').css({
				'background-color': 'transparent',
				'color': '#636466',
				});
			$('.item-menu-portifolio a').css({
				'background-color': 'transparent',
				'color': '#636466',
				});
			});
			
		$('.submenu-quem-somos').mouseleave(function(){
			$('.item-menu-quem-somos a').css({
				'background-color': 'transparent',
				'color': '#636466',
				});
			$('.item-menu-portifolio a').css({
				'background-color': '#0c5d9e',
				'padding': '10px 4px',
				'color': 'white',
				'margin': '0px 0px 0px -8px',
				'border-radius': '10px 10px 0px 0px' 
				});
			$('.submenu-quem-somos').hide();
			$('.sombra-wrapper-submenu').hide();
			$('.submenu-portifolio').show();
			$('.sombra-wrapper-submenu').show();
			});
			
		$('.item-menu-quem-somos a').mouseleave(function(){
			$('.item-menu-quem-somos a').css({
				'background-color': 'transparent',
				'color': '#636466',
				});
			$('.item-menu-portifolio a').css({
				'background-color': '#0c5d9e',
				'padding': '10px 4px',
				'color': 'white',
				'margin': '0px 0px 0px -8px',
				'border-radius': '10px 10px 0px 0px' 
				});
			$('.submenu-quem-somos').hide();
			$('.sombra-wrapper-submenu').hide();
			$('.submenu-portifolio').show();
			$('.sombra-wrapper-submenu').show();
			});
		});