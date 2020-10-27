(ns picpage-client.services.upload.views
  (:require [clojure.spec.alpha :as s]
            [reagent.core :as reagent]
            [picpage-client.domain.picture :as picture-domain]))

(defn load-image [preview-src preview-id]
  (fn [file-added-event]
    (.log js/console "load image")
    (let [file (first (array-seq (.. file-added-event -target -files)))
          file-reader (js/FileReader.)]
      (set! (.-onload file-reader)
            (fn [file-load-event]
              (reset! preview-src (.. file-load-event -target -result))
              (let [img (.getElementById js/document preview-id)]
                (set! (.-onload img)
                      (fn [img-load]
                        (.log js/console "dimensions:" (.-width img) "x" (.-height img)))))))
      (.readAsDataURL file-reader file))))

(defn image-field [image image-error]
  [:div.container>div.columns
   [:div.column.is-6
    [:div.field
     [:label.label "Picture"]
     [:div.control
      [:input.input
       {:type "file" :accept "image/*"
        :on-change
        #(let [func (load-image image "preview")]
           (reset! image nil)
           (if (func %)
             (reset! image-error false)
             (reset! image-error true)))}]]
     (when @image-error [:div.help.is-danger  "ファイルを読み込めませんでした"])]]
   (when-not (empty? @image)
     [:div.column.is-6 [:figure.image
                        [:img#preview
                         {:src @image
                          :style {:width "10rem" :object-fit "contain" :margin "auto"}}]]])])

(defn title-field [title title-error]
  [:div.field
   [:label.label "タイトル"]
   [:div.control.has-icons-left
    [:input.input {:type "text" :placeholder "タイトル (1文字 ~ 18文字)"
                   :class (if @title-error "is-danger" "is-success")
                   :on-change (fn [e]
                                (reset! title (.. e -target -value)))}]
    [:span.icon.is-samll.is-left [:i.fas.fa-user]]]
   (if @title-error
     [:div.help.is-danger
      "タイトル (4文字 ~ 18文字)"])])

(defn description-field [description description-error]
  [:div.field
   [:label.label "説明"]
   [:div.control.has-icons-left
    [:input.input {:type "text" :placeholder "説明 (0文字 ~ 256文字)"
                   :class (if @description-error "is-danger" "is-success")
                   :on-change (fn [e]
                                (reset! description (.. e -target -value)))}]
    [:span.icon.is-samll.is-left [:i.fas.fa-user]]]
   (when @description-error
     [:div.help.is-danger
      "説明 (0文字 ~ 256文字)"])])

(defn submit-field [title title-error
                    description description-error
                    image image-error
                    submitted]
  [:div.field
   [:div.control.form
    [:button.button.is-primary
     {:on-click #(do
                   (.preventDefault %)
                   (reset! title-error (not (s/valid? ::picture-domain/title @title)))
                   (reset! description-error (not (s/valid? ::picture-domain/description @description)))
                   (if (and (not @title-error)
                            (not description-error))
                     (reset! submitted true)))}
     "submit"]]
   (if @submitted [:p "submitted"])])

(def upload-content
  {:title "Picpage Upload"
   :subtitle ""
   :contents (fn []
               (let [image (reagent/atom nil)
                     image-error (reagent/atom false)
                     title (reagent/atom "")
                     title-error (reagent/atom false)
                     description (reagent/atom "")
                     description-error (reagent/atom false)
                     submitted (reagent/atom false)]
                 (fn []
                   [:div.container
                    [image-field image image-error]
                    [title-field title title-error]
                    [description-field description description-error]
                    [submit-field
                     image image-error
                     title title-error
                     description description-error
                     submitted]])))})

(def upload
  [:div.container
   [:div.titles
    [:p.title (:title upload-content)]
    [:p.subtitle (:subtitle upload-content)]]
   [:div.container.my-3>div.content {:style {:max-width "512px" :margin "auto"}}
    [(:contents upload-content)]]])
