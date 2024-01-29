
(ns pretty-elements.content-swapper.views
    (:require [fruits.hiccup.api                          :as hiccup]
              [fruits.random.api                          :as random]
              [metamorphic-content.api                    :as metamorphic-content]
              [pretty-elements.content-swapper.attributes :as content-swapper.attributes]
              [pretty-elements.content-swapper.state      :as content-swapper.state]
              [pretty-elements.engine.api                          :as pretty-elements.engine]
              [pretty-presets.api                         :as pretty-presets]
              [re-frame.api                               :as r]
              [react.api                                  :as react]
              [reagent.api                                :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- content-swapper
  ; @ignore
  ;
  ; @param (keyword) swapper-id
  ; @param (map) swapper-props
  [swapper-id swapper-props]
  [:div (content-swapper.attributes/swapper-attributes swapper-id swapper-props)
        [:div (content-swapper.attributes/swapper-body-attributes swapper-id swapper-props)
              (let [content-pool   (-> @content-swapper.state/SWAPPERS swapper-id :content-pool)
                    active-content (-> @content-swapper.state/SWAPPERS swapper-id :active-content)]
                   (letfn [(f0 [dex {:keys [id content]}] [react/mount-animation {:mounted? (= id active-content)}
                                                                                 [:div {:class :pe-content-swapper--content}
                                                                                       [metamorphic-content/compose content]]])]
                          (hiccup/put-with-indexed [:<>] content-pool f0)))]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- element-lifecycles
  ; @ignore
  ;
  ; @param (keyword) swapper-id
  ; @param (map) swapper-props
  ; {:initial-content (metamorphic-content)}
  [swapper-id {:keys [initial-content] :as swapper-props}]
  (let [initial-state {:content-pool [{:id :initial-content :content initial-content}] :active-content :initial-content}]
       ; @note (tutorials#parametering)
       (reagent/lifecycles {:component-did-mount    (fn [_ _] (swap! content-swapper.state/SWAPPERS update swapper-id merge initial-state))
                            :component-will-unmount (fn [_ _] (swap! content-swapper.state/SWAPPERS dissoc swapper-id))
                            :reagent-render         (fn [_ swapper-props] [content-swapper swapper-id swapper-props])})))
                            ; + element-did-mount, element-will-unmount

(defn element
  ; @important
  ; XXX#0517
  ; The 'content-swapper' element's content has absolute positioning.
  ; Therefore, the 'content-swapper' element and its body are stretched to their
  ; parents in order to clear space for the content because it is not doing it
  ; for itself (because its absolute positioning).
  ;
  ; @param (keyword)(opt) swapper-id
  ; @param (map) swapper-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :initial-content (metamorphic-content)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :theme (keyword)(opt)
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
            [element-lifecycles swapper-id swapper-props]))))
