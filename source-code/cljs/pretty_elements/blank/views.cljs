
(ns pretty-elements.blank.views
    (:require [fruits.random.api                :as random]
              [pretty-elements.blank.attributes :as blank.attributes]
              [pretty-elements.blank.prototypes :as blank.prototypes]
              [pretty-elements.engine.api       :as pretty-elements.engine]
              [pretty-presets.engine.api        :as pretty-presets.engine]
              [reagent.core :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- blank
  ; @ignore
  ;
  ; @param (keyword) blank-id
  ; @param (map) blank-props
  ; {:content (metamorphic-content)(opt)
  ;  ...}
  [blank-id {:keys [content] :as blank-props}]
  [:div (blank.attributes/blank-attributes blank-id blank-props)
        [:div (blank.attributes/blank-inner-attributes blank-id blank-props)
              (-> content)]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) blank-id
  ; @param (map) blank-props
  [blank-id blank-props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    blank-id blank-props))
                         :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount blank-id blank-props))
                         :reagent-render         (fn [_ blank-props] [blank blank-id blank-props])}))

(defn view
  ; @description
  ; Simplified element for displaying content.
  ;
  ; @links Implemented properties
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Content properties](pretty-core/cljs/pretty-properties/api.html#content-properties)
  ; [Inner position properties](pretty-core/cljs/pretty-properties/api.html#inner-position-properties)
  ; [Inner size properties](pretty-core/cljs/pretty-properties/api.html#inner-size-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Outer position properties](pretty-core/cljs/pretty-properties/api.html#outer-position-properties)
  ; [Outer size properties](pretty-core/cljs/pretty-properties/api.html#outer-size-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [Space properties](pretty-core/cljs/pretty-properties/api.html#space-properties)
  ; [State properties](pretty-core/cljs/pretty-properties/api.html#State-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) blank-id
  ; @param (map) blank-props
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-elements/blank.png)
  ; [blank {:content [:div "My content"]}]
  ([blank-props]
   [view (random/generate-keyword) blank-props])

  ([blank-id blank-props]
   ; @note (tutorials#parameterizing)
   (fn [_ blank-props]
       (let [blank-props (pretty-presets.engine/apply-preset     blank-id blank-props)
             blank-props (blank.prototypes/blank-props-prototype blank-id blank-props)]
            [view-lifecycles blank-id blank-props]))))
