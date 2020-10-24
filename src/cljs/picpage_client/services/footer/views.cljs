(ns picpage-client.services.footer.views)

(def footer-content
  [:p
   [:strong "Picpage"] " by "
   [:a {:href "mailto:meguru.mokke@gmail.com"} "物怪 巡"] ". The website content is licensed "
   [:a {:href "ehttp://creativecommons.org/licenses/by-nc-sa/4.0/"} "CC BY NC SA 4.0"]])

(def footer
  [:footer.footer>div.content.has-text-centered
   footer-content])
