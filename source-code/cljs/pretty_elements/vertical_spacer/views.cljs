
(ns pretty-elements.vertical-spacer.views
    (:require [fruits.random.api                          :as random]
              [pretty-elements.vertical-spacer.attributes :as vertical-spacer.attributes]
              [pretty-elements.vertical-spacer.prototypes :as vertical-spacer.prototypes]
              [pretty-elements.engine.api                          :as pretty-elements.engine]
              [pretty-presets.engine.api :as pretty-presets.engine]
              [reagent.api                                :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- vertical-spacer
  ; @ignore
  ;
  ; @param (keyword) spacer-id
  ; @param (map) spacer-props
  [spacer-id spacer-props]
  [:div (vertical-spacer.attributes/spacer-attributes spacer-id spacer-props)])

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
                       :reagent-render         (fn [_ spacer-props] [vertical-spacer spacer-id spacer-props])}))

(defn view
  ; @param (keyword)(opt) spacer-id
  ; @param (map) spacer-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :height (keyword, px or string)(opt)
  ;   Default: :parent
  ;  :on-mount-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :theme (keyword)(opt)       ?????
  ;  :width (keyword, px or string)(opt)
  ;   Default: :s}
  ;
  ; @usage
  ; [vertical-spacer {...}]
  ;
  ; @usage
  ; [vertical-spacer :my-vertical-spacer {...}]
  ([spacer-props]
   [view (random/generate-keyword) spacer-props])

  ([spacer-id spacer-props]
   ; @note (tutorials#parametering)
   (fn [_ spacer-props]
       (let [spacer-props (pretty-presets.engine/apply-preset                spacer-id spacer-props)
             spacer-props (vertical-spacer.prototypes/spacer-props-prototype spacer-id spacer-props)]
            [view-lifecycles spacer-id spacer-props]))))
