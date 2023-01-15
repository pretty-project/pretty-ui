
(ns components.vector-items-header.views
    (:require [components.vector-items-header.helpers    :as vector-items-header.helpers]
              [components.vector-items-header.prototypes :as vector-items-header.prototypes]
              [elements.api                              :as elements]
              [random.api                                :as random]
              [vector.api                                :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- vector-items-add-button
  ; @param (keyword) header-id
  ; @param (map) header-props
  ; {:initial-item (*)
  ;  :value-path (vector)}
  [_ {:keys [initial-item value-path]}]
  [elements/icon-button {:border-radius :xl
                         :hover-color   "#e0d7ff"
                         :icon          :add_circle
                         :on-click      [:x.db/apply-item! value-path vector/conj-item initial-item]
                         :tooltip       :add!}])

(defn- vector-items-header-body
  ; @param (keyword) header-id
  ; @param (map) header-props
  ; {:label (metamorphic-contect)(opt)}
  [header-id {:keys [label] :as header-props}]
  [:div.c-vector-items-header--body (vector-items-header.helpers/header-body-attributes header-id header-props)
                                    [elements/label {:content label :font-size :xl}]
                                    [vector-items-add-button header-id header-props]])

(defn- vector-items-header
  ; @param (keyword) header-id
  ; @param (map) header-props
  [header-id header-props]
  [:div.c-vector-items-header (vector-items-header.helpers/header-attributes header-id header-props)
                              [vector-items-header-body                      header-id header-props]])

(defn component
  ; @param (keyword)(opt) header-id
  ; @param (map) header-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :horizontal-align (keyword)(opt)
  ;   :center, :left, :right
  ;   Default: :left
  ;  :indent (map)(opt)
  ;  :initial-item (*)(opt)
  ;   Default: {}
  ;  :label (metamorphic-content)
  ;  :outdent (map)(opt)
  ;  :style (map)(opt)
  ;  :value-path (vector)}
  ;
  ; @usage
  ; [vector-items-header {...}]
  ;
  ; @usage
  ; [vector-items-header :my-vector-items-header {...}]
  ([header-props]
   [component (random/generate-keyword) header-props])

  ([header-id header-props]
   (let [header-props (vector-items-header.prototypes/header-props-prototype header-props)]
        [vector-items-header header-id header-props])))
