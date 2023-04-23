
(ns elements.content-swapper.views
    (:require [elements.content-swapper.attributes :as content-swapper.attributes]
              [elements.content-swapper.prototypes :as content-swapper.prototypes]
              [elements.content-swapper.state      :as content-swapper.state]
              [metamorphic-content.api             :as metamorphic-content]
              [random.api                          :as random]
              [re-frame.api                        :as r]
              [react.api                           :as react]
              [reagent.api                         :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- content-swapper-structure
  ; @ignore
  ;
  ; @param (keyword) swapper-id
  ; @param (map) swapper-props
  [swapper-id swapper-props]
  [:div (content-swapper.attributes/swapper-attributes swapper-id swapper-props)
        [:div (content-swapper.attributes/swapper-body-attributes swapper-id swapper-props)
              (let [current-page (-> @content-swapper.state/SWAPPERS swapper-id :current-page)]
                   [:<> [react/mount-animation {:mounted? (= current-page :a)}
                                               [:div {:class :e-content-swapper--page}
                                                     (let [page-a (-> @content-swapper.state/SWAPPERS swapper-id :page-a)]
                                                          [metamorphic-content/compose page-a])]]
                        [react/mount-animation {:mounted? (= current-page :b)}
                                               [:div {:class :e-content-swapper--page}
                                                     (let [page-b (-> @content-swapper.state/SWAPPERS swapper-id :page-b)]
                                                          [metamorphic-content/compose page-b])]]])]])

(defn- content-swapper
  ; @ignore
  ;
  ; @param (keyword) swapper-id
  ; @param (map) swapper-props
  ; {}
  [swapper-id {:keys [default-page] :as swapper-props}]
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (swap! content-swapper.state/SWAPPERS update swapper-id merge {:current-page :a :page-a default-page}))
                       :component-will-unmount (fn [_ _] (swap! content-swapper.state/SWAPPERS dissoc swapper-id))
                       :reagent-render         (fn [_ _] [content-swapper-structure swapper-id swapper-props])}))

(defn element
  ; @warning
  ; XXX#0517
  ; The content-swapper element pages have absolute positioning, therefore
  ; the content-swapper element and its body stretch to their parents in order to
  ; make space for the pages.
  ;
  ; @param (keyword)(opt) swapper-id
  ; @param (map) swapper-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :default-page (metamorphic-content)(opt)
  ;  :indent (map)(opt)
  ;   {:bottom (keyword)(opt)
  ;    :left (keyword)(opt)
  ;    :right (keyword)(opt)
  ;    :top (keyword)(opt)
  ;    :horizontal (keyword)(opt)
  ;    :vertical (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
  ;  :outdent (map)(opt)
  ;   Same as the :indent property
  ;  :style (map)(opt)}
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
