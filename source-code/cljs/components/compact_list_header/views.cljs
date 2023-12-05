
(ns components.compact-list-header.views
    (:require [components.compact-list-header.attributes :as compact-list-header.attributes]
              [components.compact-list-header.prototypes :as compact-list-header.prototypes]
              [pretty-elements.api                       :as pretty-elements]
              [random.api                                :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn compact-list-header
  ; @param (keyword) header-id
  ; @param (map) header-props
  ; {}
  [header-id {:keys [hide-button order-button search-field] :as header-props}]
  [:div (compact-list-header.attributes/header-attributes header-id header-props)
        [:div (compact-list-header.attributes/header-body-attributes header-id header-props)
              [pretty-elements/icon-button order-button]
              [pretty-elements/text-field  search-field]
              [pretty-elements/icon-button hide-button]]])

(defn component
  ; @param (keyword)(opt) header-id
  ; @param (map) header-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :hide-button (map)
  ;  :indent (map)(opt)
  ;  :label (metamorphic-content)
  ;  :order-button (map)
  ;  :outdent (map)(opt)
  ;  :search-field (map)
  ;  :style (map)(opt)}
  ;
  ; @usage
  ; [compact-list-header {...}]
  ;
  ; @usage
  ; [compact-list-header :my-compact-list-header {...}]
  ([header-props]
   [component (random/generate-keyword) header-props])

  ([header-id header-props]
   (fn [_ header-props] ; XXX#0106 (README.md#parametering)
       (let [header-props (compact-list-header.prototypes/header-props-prototype header-props)]
            [compact-list-header header-id header-props]))))
