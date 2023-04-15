
(ns elements.content-swapper.views
    (:require [elements.button.views               :as button.views]
              [elements.content-swapper.attributes :as content-swapper.attributes]
              [elements.content-swapper.env        :as content-swapper.env]
              [elements.content-swapper.prototypes :as content-swapper.prototypes]
              [elements.content-swapper.state      :as content-swapper.state]
              [elements.label.views                :as label.views]
              [hiccup.api                          :as hiccup]
              [metamorphic-content.api             :as metamorphic-content]
              [noop.api                            :refer [return]]
              [random.api                          :as random]
              [re-frame.api                        :as r]
              [reagent.api                         :as reagent]
              [window-observer.api                 :as window-observer]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- content-swapper-label
  ; @ignore
  ;
  ; @param (keyword) swapper-id
  ; @param (map) swapper-props
  ; {}
  [_ {:keys [label]}]
  [label.views/element {:border-color     :highlight
                        :border-position  :bottom
                        :content          label
                        :horizontal-align :left
                        :icon             :more_vert
                        :icon-color       :highlight
                        :icon-size        :l
                        :indent           {:all :xs}
                        :width            :parent}])

(defn- content-swapper-back-button
  ; @ignore
  ;
  ; @param (keyword) swapper-id
  ; @param (map) swapper-props
  ; {}
  [swapper-id {:keys [label]}]
  (letfn [(f [] (swap! content-swapper.state/SWAPPERS dissoc swapper-id))]
         [button.views/element {:border-color     :highlight
                                :border-position  :bottom
                                :horizontal-align :left
                                :icon             :arrow_back
                                :icon-size        :l
                                :indent           {:all :xs}
                                :label            label
                                :on-click         #(f)
                                :width            :parent}]))

(defn- content-swapper-page-button
  ; @ignore
  ;
  ; @param (keyword) swapper-id
  ; @param (map) swapper-props
  ; @param (integer) page-dex
  ; @param (map) page-props
  ; {}
  [swapper-id _ page-dex {:keys [label]}]
  ; XXX#1239 (source-code/cljs/elements/dropdown_menu/prototypes.cljs)
  (letfn [(f [] (swap! content-swapper.state/SWAPPERS assoc-in [swapper-id :active-dex] page-dex)
                (return nil))]
         [button.views/element {:label            label
                                :horizontal-align :left
                                :icon             :chevron_right
                                :icon-position    :right
                                :icon-size        :xl
                                :gap              :auto
                                :on-click         #(f)
                                :width            :parent}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- content-swapper-swapping-content
  ; @ignore
  ;
  ; @param (keyword) swapper-id
  ; @param (map) swapper-props
  ; {}
  [swapper-id {:keys [pages] :as swapper-props}]
  [:<> [:div {:class :e-content-swapper--header}
             (if-let [swapping-content (content-swapper.env/get-swapping-content swapper-id swapper-props)]
                     [content-swapper-back-button swapper-id swapper-props]
                     [content-swapper-label       swapper-id swapper-props])]
       [:div (content-swapper.attributes/swapper-body-attributes swapper-id swapper-props)
             (if-let [swapping-content (content-swapper.env/get-swapping-content swapper-id swapper-props)]

                     ; Selected page
                     [:<>
                          [:div {:class :e-content-swapper--page}
                                [metamorphic-content/compose swapping-content]]]

                     ; Page buttons
                     ; The page buttons displayed on a page to give them a container
                     ; with the same size attributes as of content pages
                     (letfn [(f [page-dex page-props] [content-swapper-page-button swapper-id swapper-props page-dex page-props])]
                            [:div {:class :e-content-swapper--page}
                                  [:div {:class :e-content-swapper--page-buttons}
                                        (hiccup/put-with-indexed [:<>] pages f)]]))]])

(defn- content-swapper-unfolded-content
  ; @ignore
  ;
  ; @param (keyword) swapper-id
  ; @param (map) swapper-props
  ; {}
  [swapper-id {:keys [pages] :as swapper-props}]
  [:div (content-swapper.attributes/swapper-body-attributes swapper-id swapper-props)
        (letfn [(f [{:keys [content]}] [:div {:class :e-content-swapper--page}
                                             [metamorphic-content/compose content]])]
               (hiccup/put-with [:<>] pages f))])

(defn- content-swapper-structure
  ; @ignore
  ;
  ; @param (keyword) swapper-id
  ; @param (map) swapper-props
  ; {}
  [swapper-id {:keys [threshold] :as swapper-props}]
  ; If the viewport wider than the specified threshold, the pages are displayed
  ; unfolded near by each other
  [:div (content-swapper.attributes/swapper-attributes swapper-id swapper-props)
        (if (window-observer/viewport-width-min? threshold)
            [content-swapper-unfolded-content swapper-id swapper-props]
            [content-swapper-swapping-content swapper-id swapper-props])])

(defn- content-swapper
  ; @ignore
  ;
  ; @param (keyword) swapper-id
  ; @param (map) swapper-props
  ; {}
  [swapper-id {:keys [on-mount on-unmount] :as swapper-props}]
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (r/dispatch on-mount))
                       :component-will-unmount (fn [_ _] (r/dispatch on-unmount)
                                                         (swap! content-swapper.state/SWAPPERS dissoc swapper-id))
                       :reagent-render         (fn [_ _] [content-swapper-structure swapper-id swapper-props])}))

(defn element
  ; @param (keyword)(opt) swapper-id
  ; @param (map) swapper-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :gap (keyword)(opt)
  ;   Distance between the unfolded pages
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl, :auto
  ;  :height (keyword)(opt)
  ;   :auto, :content, :parent, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :content
  ;  :indent (map)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  :max-height (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;  :max-width (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;  :min-height (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;  :min-width (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;  :outdent (map)(opt)
  ;  :pages (maps in vector)
  ;   [{:content (metamorphic-content)
  ;     :label (metamorphic-content)}]
  ;  :style (map)(opt)
  ;  :threshold (px)(opt)
  ;  :width (keyword)(opt)
  ;   :auto, :content, :parent, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :content}
  ;
  ; @usage
  ; [content-swapper {...}]
  ;
  ; @usage
  ; [content-swapper :my-content-swapper {...}]
  ([swapper-props]
   [element (random/generate-keyword) swapper-props])

  ([swapper-id swapper-props]
   (let [swapper-props (content-swapper.prototypes/swapper-props-prototype swapper-props)]
        [content-swapper swapper-id swapper-props])))
