
(ns elements.content-swapper.views
    (:require [candy.api :refer [return]]
              [elements.button.views               :as button.views]
              [elements.content-swapper.attributes :as content-swapper.attributes]
              [elements.content-swapper.prototypes :as content-swapper.prototypes]
              [elements.content-swapper.state      :as content-swapper.state]
              [random.api                          :as random]
              [re-frame.api                        :as r]
              [reagent.api                         :as reagent]
              [x.components.api                    :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- content-swapper-back-button
  ; @ignore
  ;
  ; @param (keyword) swapper-id
  ; @param (map) swapper-props
  ; @param (map) page
  ; {}
  [swapper-id _ {:keys [label]}]
  (letfn [(f [] (swap! content-swapper.state/VISIBLE-PAGE dissoc swapper-id))]
         [button.views/element {:horizontal-align :left
                                :icon             :arrow_back
                                :on-click         #(f)
                                :outdent          {:bottom :xs}}]))

(defn- content-swapper-page-button
  ; @ignore
  ;
  ; @param (keyword) swapper-id
  ; @param (map) swapper-props
  ; @param (map) page
  ; {}
  [swapper-id _ {:keys [label] :as page}]
  ; XXX#1239 (source-code/cljs/elements/dropdown_menu/prototypes.cljs)
  (letfn [(f [page] (swap! content-swapper.state/VISIBLE-PAGE assoc swapper-id page)
                    (return nil))]
         [button.views/element {:label            label
                                :horizontal-align :left
                                :icon             :chevron_right
                                :icon-position    :right
                                :gap              :auto
                                :on-click         #(f page)}]))

(defn- content-swapper-swapping-content
  ; @ignore
  ;
  ; @param (keyword) swapper-id
  ; @param (map) swapper-props
  ; {}
  [swapper-id {:keys [pages] :as swapper-props}]
  [:div (content-swapper.attributes/swapper-body-attributes swapper-id swapper-props)
        (if-let [page (swapper-id @content-swapper.state/VISIBLE-PAGE)]

                ; Selected page
                [:div (content-swapper.attributes/swapper-page-attributes swapper-id swapper-props)
                      [:div {:class :e-content-swapper--page-header}
                            [content-swapper-back-button swapper-id swapper-props page]]
                      [x.components/content swapper-id (:content page)]]

                ; Page buttons
                ; The page buttons displayed on a page to give them a container
                ; with the same size attributes as of content pages
                (letfn [(f [button-list {:keys [label] :as page}]
                           (conj button-list [content-swapper-page-button swapper-id swapper-props page]))]
                       [:div (content-swapper.attributes/swapper-page-attributes swapper-id swapper-props)
                             [:div {:class :e-content-swapper--page-buttons}
                                   (reduce f [:<>] pages)]]))])

(defn- content-swapper-unfolded-content
  ; @ignore
  ;
  ; @param (keyword) swapper-id
  ; @param (map) swapper-props
  ; {}
  [swapper-id {:keys [pages] :as swapper-props}]
  [:div (content-swapper.attributes/swapper-body-attributes swapper-id swapper-props)
        (letfn [(f [page-list {:keys [content]}]
                   (conj page-list [:div (content-swapper.attributes/swapper-page-attributes swapper-id swapper-props)
                                         [x.components/content swapper-id content]]))]
               (reduce f [:<>] pages))])

(defn- content-swapper-structure
  ; @ignore
  ;
  ; @param (keyword) swapper-id
  ; @param (map) swapper-props
  ; {}
  [swapper-id {:keys [threshold] :as swapper-props}]
  ; If the viewport width is wider than the specified threshold,
  ; the pages are displayed unfolded near by each other
  [:div (content-swapper.attributes/swapper-attributes swapper-id swapper-props)
        (if @(r/subscribe [:x.environment/viewport-min? threshold])
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
                                                         (swap! content-swapper.state/VISIBLE-PAGE dissoc swapper-id))
                       :reagent-render         (fn [_ _] [content-swapper-structure swapper-id swapper-props])}))

(defn element
  ; @description
  ; The :max-height, :max-width, :min-height and :min-width properties
  ; are applied on each page.
  ;
  ; @param (keyword)(opt) swapper-id
  ; @param (map) swapper-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :gap (keyword)(opt)
  ;   Distance between unfolded pages
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl, :auto
  ;  :indent (map)(opt)
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
  ;  :threshold (px)(opt)}
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
