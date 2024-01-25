
(ns pretty-elements.icon.views
    (:require [fruits.random.api               :as random]
              [pretty-elements.icon.attributes :as icon.attributes]
              [pretty-elements.icon.prototypes :as icon.prototypes]
              [pretty-presets.api              :as pretty-presets]
              [pretty-engine.api :as pretty-engine]
              [reagent.api :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- icon
  ; @ignore
  ;
  ; @param (keyword) icon-id
  ; @param (map) icon-props
  ; {:icon (keyword)}
  [icon-id {:keys [icon] :as icon-props}]
  [:div (icon.attributes/icon-attributes icon-id icon-props)
        [:i (icon.attributes/icon-body-attributes icon-id icon-props) icon]])

(defn element
  ; @param (keyword)(opt) icon-id
  ; @param (map) icon-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :icon (keyword)
  ;  :icon-color (keyword or string)(opt)
  ;   Default: :default
  ;  :icon-family (keyword)(opt)
  ;   Default: :material-symbols-outlined
  ;  :icon-size (keyword, px or string)(opt)
  ;   Default: :m
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)}
  ;
  ; @usage
  ; [icon {...}]
  ;
  ; @usage
  ; [icon :my-icon {...}]
  ([icon-props]
   [element (random/generate-keyword) icon-props])

  ([icon-id icon-props]
   ; @note (tutorials#parametering)
   (fn [_ icon-props]
       (let [icon-props (pretty-presets/apply-preset          icon-props)
             icon-props (icon.prototypes/icon-props-prototype icon-props)]
            [icon icon-id icon-props]))))
