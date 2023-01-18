
(ns components.vector-items-header.views
    (:require [components.vector-items-header.attributes :as vector-items-header.attributes]
              [components.vector-items-header.prototypes :as vector-items-header.prototypes]
              [elements.api                              :as elements]
              [random.api                                :as random]
              [vector.api                                :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- add-icon-button
  ; @param (keyword) header-id
  ; @param (map) header-props
  ; {}
  [_ {:keys [initial-item on-change value-path] :as header-props}]
  (let [add-event [:x.db/apply-item! value-path vector/conj-item initial-item]]
       [elements/icon-button {:border-radius   {:all :xl}
                              :color           :secondary
                              :hover-color     :highlight
                              :icon            :add_circle
                              :on-click        {:dispatch-n [add-event on-change]}
                              :tooltip-content :add!}]))

(defn- vector-items-header
  ; @param (keyword) header-id
  ; @param (map) header-props
  ; {}
  [header-id {:keys [label] :as header-props}]
  [:div (vector-items-header.attributes/header-attributes header-id header-props)
        [:div (vector-items-header.attributes/header-body-attributes header-id header-props)
              [elements/label {:content label :font-size :xl}]
              [add-icon-button header-id header-props]]])

(defn component
  ; @param (keyword)(opt) header-id
  ; @param (map) header-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :horizontal-align (keyword)(opt)
  ;   :center, :left, :right
  ;   Default: :center
  ;  :indent (map)(opt)
  ;  :initial-item (*)(opt)
  ;   Default: {}
  ;  :label (metamorphic-content)
  ;  :on-change (metamorphic-event)(opt)
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
