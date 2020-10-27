(ns picpage-client.services.home.views)

(def home-content
  {:title "PicPage はフリーの画像投稿サイトです"
   :subtitle "(簡易の Web 開発のチュートリアルとして作成されています。)"
   :contents (fn []
               [:div
                [:p.is-size-4 "簡単な説明"]
                [:ul
                 [:li "サインアップ or ログインすると、画像を投稿できるようになります。"]
                 [:li "ユーザの検索は、上部の検索バー (モバイル端末の場合は、ハンバーガーメニュー内の検索バー) より行えます。"]
                 [:li "特定の画像の共有は、 URL を直接共有して下さい。"]]])})

(def home
  [:div.container
   [:div.titles
    [:p.title (:title home-content)]
    [:p.subtitle.is-6 (:subtitle home-content)]]
   [:div.container.my-3>div.content
    [(:contents home-content)]]])
