
(ns components.vector-items-header.views
    (:require [components.vector-items-header.attributes :as vector-items-header.attributes]
              [components.vector-items-header.prototypes :as vector-items-header.prototypes]
              [fruits.random.api                         :as random]
              [fruits.vector.api                         :as vector]
              [pretty-elements.api                       :as pretty-elements]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- add-icon-button
  ; @ignore
  ;
  ; @param (keyword) header-id
  ; @param (map) header-props
  ; {}
  [_ {:keys [initial-item on-change value-path] :as header-props}]
  (let [add-event [:update-item! value-path vector/conj-item initial-item]]
       [pretty-elements/icon-button {:border-radius   {:all :xl}
                                     :color           :secondary
                                     :hover-color     :highlight
                                     :icon            :add_circle
                                     :on-click        {:dispatch-n [add-event on-change]}
                                     :tooltip-content :add!}]))

(defn- vector-items-header
  ; @ignore
  ;
  ; @param (keyword) header-id
  ; @param (map) header-props
  ; {}
  [header-id {:keys [label] :as header-props}]
  [:div (vector-items-header.attributes/header-attributes header-id header-props)
        [:div (vector-items-header.attributes/header-body-attributes header-id header-props)
              [pretty-elements/label {:content label :font-size :xl}]
              [add-icon-button header-id header-props]]])

(defn view
  ; @param (keyword)(opt) header-id
  ; @param (map) header-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :horizontal-align (keyword)(opt)
  ;   Default: :center
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :initial-item (*)(opt)
  ;   Default: {}
  ;  :label (metamorphic-content)
  ;  :on-change (Re-Frame metamorphic-event)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :style (map)(opt)
  ;  :value-path (Re-Frame path vector)}
  ;
  ; @usage
  ; [vector-items-header {...}]
  ;
  ; @usage
  ; [vector-items-header :my-vector-items-header {...}]
  ([header-props]
   [view (random/generate-keyword) header-props])

  ([header-id header-props]
   ; @note (tutorials#parametering)
   (fn [_ header-props]
       (let [header-props (vector-items-header.prototypes/header-props-prototype header-props)]
            [vector-items-header header-id header-props]))))
