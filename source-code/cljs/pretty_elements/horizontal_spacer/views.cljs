
(ns pretty-elements.horizontal-spacer.views
    (:require [fruits.random.api                            :as random]
              [pretty-elements.horizontal-spacer.attributes :as horizontal-spacer.attributes]
              [pretty-elements.horizontal-spacer.prototypes :as horizontal-spacer.prototypes]
              [pretty-elements.engine.api                            :as pretty-elements.engine]
              [pretty-presets.engine.api :as pretty-presets.engine]
              [reagent.api                                  :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- horizontal-spacer
  ; @ignore
  ;
  ; @param (keyword) spacer-id
  ; @param (map) spacer-props
  [spacer-id spacer-props]
  [:div (horizontal-spacer.attributes/spacer-attributes spacer-id spacer-props)])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) spacer-id
  ; @param (map) spacer-props
  [spacer-id spacer-props]
  ; @note (tutorials#parametering)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    spacer-id spacer-props))
                       :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount spacer-id spacer-props))
                       :reagent-render         (fn [_ spacer-props] [horizontal-spacer spacer-id spacer-props])}))

(defn view
  ; @param (keyword)(opt) spacer-id
  ; @param (map) spacer-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :height (keyword, px or string)(opt)
  ;   Default: :s
  ;  :on-mount-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :width (keyword, px or string)(opt)
  ;   Default: :auto}
  ;
  ; @usage
  ; [horizontal-spacer {...}]
  ;
  ; @usage
  ; [horizontal-spacer :my-horizontal-spacer {...}]
  ([spacer-props]
   [view (random/generate-keyword) spacer-props])

  ([spacer-id spacer-props]
   ; @note (tutorials#parametering)
   (fn [_ spacer-props]
       (let [spacer-props (pretty-presets.engine/apply-preset                  spacer-id spacer-props)
             spacer-props (horizontal-spacer.prototypes/spacer-props-prototype spacer-id spacer-props)]
            [view-lifecycles spacer-id spacer-props]))))
