
(ns pretty-elements.icon.views
    (:require [fruits.random.api               :as random]
              [pretty-elements.engine.api      :as pretty-elements.engine]
              [pretty-elements.icon.attributes :as icon.attributes]
              [pretty-elements.icon.prototypes :as icon.prototypes]
              [pretty-presets.engine.api       :as pretty-presets.engine]
              [reagent.core :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- icon
  ; @ignore
  ;
  ; @param (keyword) icon-id
  ; @param (map) icon-props
  ; {:icon (keyword)(opt)
  ;  ...}
  [icon-id {:keys [icon] :as icon-props}]
  [:div (icon.attributes/icon-attributes icon-id icon-props)
        [:div (icon.attributes/icon-inner-attributes icon-id icon-props)
            [:i (icon.attributes/icon-content-attributes icon-id icon-props) icon]]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) icon-id
  ; @param (map) icon-props
  [icon-id icon-props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    icon-id icon-props))
                         :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount icon-id icon-props))
                         :reagent-render         (fn [_ icon-props] [icon icon-id icon-props])}))

(defn view
  ; @description
  ; Icon element.
  ;
  ; @links Implemented properties
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Flex properties](pretty-core/cljs/pretty-properties/api.html#flex-properties)
  ; [Icon properties](pretty-core/cljs/pretty-properties/api.html#icon-properties)
  ; [Inner position properties](pretty-core/cljs/pretty-properties/api.html#inner-position-properties)
  ; [Inner size properties](pretty-core/cljs/pretty-properties/api.html#inner-size-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Outer position properties](pretty-core/cljs/pretty-properties/api.html#outer-position-properties)
  ; [Outer size properties](pretty-core/cljs/pretty-properties/api.html#outer-size-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [Space properties](pretty-core/cljs/pretty-properties/api.html#space-properties)
  ; [State properties](pretty-core/cljs/pretty-properties/api.html#state-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) icon-id
  ; @param (map) icon-props
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-elements/icon.png)
  ; [icon {:icon :settings}]
  ([icon-props]
   [view (random/generate-keyword) icon-props])

  ([icon-id icon-props]
   ; @note (tutorials#parameterizing)
   (fn [_ icon-props]
       (let [icon-props (pretty-presets.engine/apply-preset   icon-id icon-props)
             icon-props (icon.prototypes/icon-props-prototype icon-id icon-props)]
            [view-lifecycles icon-id icon-props]))))
