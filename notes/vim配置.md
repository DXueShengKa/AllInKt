## vim 配置

```
"语法高亮
syntax on
"显示行号
set number relativenumber
"显示光标所在位置的行号和列号
set ruler


set keep-english-in-normal

" 复制到系统剪贴板
vnoremap <C-c> \"+y<ESC>
" 从系统剪贴板取出
inoremap <C-v> <ESC>\"+pA
" 跳转到当前行第一个非空字符
nnoremap <space>; ^
" 跳转到行尾
nnoremap <space>' $

" Plug 'mg979/vim-visual-multi', {'branch': 'master'}

" 上移动一行
map <C-k> :m -2<CR>
" 下移动一行
map <C-j> :m +1<CR>
" 替换选中
vnoremap <C-r> \y :%s /<C-r>0/
```