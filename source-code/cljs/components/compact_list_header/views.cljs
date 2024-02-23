
(ns components.compact-list-header.views
    (:require [components.compact-list-header.attributes :as compact-list-header.attributes]
              [components.compact-list-header.prototypes :as compact-list-header.prototypes]
              [fruits.random.api                         :as random]
              [pretty-elements.api                       :as pretty-elements]
              [pretty-inputs.api                         :as pretty-inputs]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn compact-list-header
  ; @param (keyword) header-id
  ; @param (map) header-props
  ; {}
  [header-id {:keys [hide-button order-button search-field] :as header-props}]
  [:div (compact-list-header.attributes/header-attributes header-id header-props)
        [:div (compact-list-header.attributes/header-inner-attributes header-id header-props)
              [pretty-elements/icon-button order-button]
              [pretty-inputs/text-field    search-field]
              [pretty-elements/icon-button hide-button]]])

(defn view
  ; @param (keyword)(opt) header-id
  ; @param (map) header-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :hide-button (map)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :label (metamorphic-content)
  ;  :order-button (map)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :search-field (map)
  ;  :style (map)(opt)}
  ;
  ; @usage
  ; [compact-list-header {...}]
  ;
  ; @usage
  ; [compact-list-header :my-compact-list-header {...}]
  ([header-props]
   [view (random/generate-keyword) header-props])

  ([header-id header-props]
   ; @note (tutorials#parameterizing)
   (fn [_ header-props]
       (let [header-props (compact-list-header.prototypes/header-props-prototype header-props)]
            [compact-list-header header-id header-props]))))
