  nohup ./GrabPage.sh http://wiki.livedoor.jp/inatekken/d/%c1%ed%b9%e7TUD /home/liye/www/wiki/tt2 &
  nohup ./GrabPage.sh http://wiki.livedoor.jp/inatekken/d/%c1%ed%b9%e7TBR ~/www/wiki/br &
  nohup ./GrabPage.sh http://wiki.livedoor.jp/inatekken/d/%c1%ed%b9%e7T6 ~/www/wiki/t6 &
  
  抓取网页
  me.liye.tekken.wiki.grab.GrabPage
  翻译文件名
  me.liye.tekken.wiki.trans.TransFileName


  抓取图片
https://eng.tekkenpedia.com/wiki/Tekken_7
  sed -n 's/.*srcset="\([^"]*\).*/\1/p' pic | awk -F\  '{print "https://eng.tekkenpedia.com"$1}' |xargs wget {}
