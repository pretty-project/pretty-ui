
(ns pretty-elements.content-swapper.views
    (:require [fruits.hiccup.api                          :as hiccup]
              [fruits.random.api                          :as random]
              [metamorphic-content.api                    :as metamorphic-content]
              [pretty-elements.content-swapper.attributes :as content-swapper.attributes]
              [pretty-elements.content-swapper.state      :as content-swapper.state]
              [pretty-presets.api                         :as pretty-presets]
              [re-frame.api                               :as r]
              [react.api                                  :as react]
              [reagent.api                                :as reagent]))

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
              (let [page-pool   (-> @content-swapper.state/SWAPPERS swapper-id :page-pool)
                    active-page (-> @content-swapper.state/SWAPPERS swapper-id :active-page)]
                   (letfn [(f0 [dex {:keys [id page]}] [react/mount-animation {:mounted? (= id active-page)}
                                                                              [:div {:class :pe-content-swapper--page}
                                                                                    [metamorphic-content/compose page]]])]
                          (hiccup/put-with-indexed [:<>] page-pool f0)))]])

(defn- content-swapper
  ; @ignore
  ;
  ; @param (keyword) swapper-id
  ; @param (map) swapper-props
  ; {:initial-page (metamorphic-content)}
  [swapper-id {:keys [initial-page] :as swapper-props}]
  (let [initial-state {:page-pool [{:id :initial-page :page initial-page}] :active-page :initial-page}]
       ; @note (tutorials#parametering)
       (reagent/lifecycles {:component-did-mount    (fn [_ _] (swap! content-swapper.state/SWAPPERS update swapper-id merge initial-state))
                            :component-will-unmount (fn [_ _] (swap! content-swapper.state/SWAPPERS dissoc swapper-id))
                            :reagent-render         (fn [_ swapper-props] [content-swapper-structure swapper-id swapper-props])})))

(defn element
  ; @important
  ; XXX#0517
  ; The 'content-swapper' element's pages have absolute positioning.
  ; Therefore, the 'content-swapper' element and its body are stretched to their
  ; parents in order to clear space for the pages because they are not doing it
  ; for themeself (because their absolute positioning).
  ;
  ; @param (keyword)(opt) swapper-id
  ; @param (map) swapper-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :indent (map)(opt)
  ;   {:bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :initial-page (metamorphic-content)
  ;  :outdent (map)(opt)
  ;   {:bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :preset (keyword)(opt)
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
   ; @note (tutorials#parametering)
   (fn [_ swapper-props]
       (let [ ; swapper-props (content-swapper.prototypes/swapper-props-prototype swapper-props)
             swapper-props (pretty-presets/apply-preset swapper-props)]
            [content-swapper swapper-id swapper-props]))))
