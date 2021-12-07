
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.16
; Description:
; Version: v0.3.6
; Compatibility: x4.4.7



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-layouts.layout-b
    (:require [mid-fruits.candy     :refer [param return]]
              [mid-fruits.keyword   :as keyword]
              [mid-fruits.map       :as map]
              [mid-fruits.vector    :as vector]
              [x.app-components.api :as components]
              [x.app-core.api       :as a]
              [x.app-elements.api   :as elements]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- layout-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) layout-props
  ;
  ; @return (map)
  ;  {:min-width (keyword)}
  [layout-props]
  (merge {}
         (param layout-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- card-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) layout-id
  ; @param (map) layout-props
  ; @param (map) card-props
  ;  {:icon (keyword)(opt)
  ;   :label (metamorphic-content)}
  ;
  ; @return (component)
  [_ _ card-props]
  (let [label-props (map/rekey-item card-props :label :content)]
       [elements/label label-props]))

(defn- card
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) layout-id
  ; @param (map) layout-props
  ; @param (map) card-props
  ;  {:badge-color (keyword)(opt)
  ;   :on-click (metamorphic-event)(opt)}
  ;
  ; @return (component)
  [layout-id layout-props {:keys [badge-color on-click] :as card-props}]
  [elements/card {:content [card-label layout-id layout-props card-props]
                  :horizontal-align :left :on-click on-click :badge-color badge-color
                  :min-width :m}])

(defn- card-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) layout-id
  ; @param (map) layout-props
  ;  {:cards (maps in vector)}
  ;
  ; @return (component)
  [layout-id {:keys [cards] :as layout-props}]
  (reduce (fn [card-list card-props]
              (vector/conj-item card-list [card layout-id layout-props card-props]))
          [:div.x-layout-b--card-group]
          (param cards)))

(defn- layout-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) layout-id
  ; @param (map) layout-props
  ;
  ; @return (component)
  [layout-id layout-props]
  [card-list layout-id layout-props])

(defn- layout-b
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) layout-id
  ; @param (map) layout-props
  ;  {}
  ;
  ; @return (component)
  [layout-id layout-props]
  [layout-body layout-id layout-props])

(defn layout
  ; @param (keyword)(opt) layout-id
  ; @param (map) layout-props
  ;  {:cards (maps in vector)
  ;    [{:badge-color (keyword)(opt)
  ;       :primary, :secondary, :warning, :success
  ;      :icon (keyword)(opt)
  ;      :icon-family (keyword)(opt)
  ;       Default: :material-icons-filled
  ;       Only w/ {:icon ...}
  ;      :label (metamorphic-content)
  ;      :on-click (metamorphic-event)(opt)}
  ;     {...}]}
  ;
  ; @usage
  ;  [layouts/layout-b {...}]
  ;
  ; @usage
  ;  [layouts/layout-b :my-layout {...}]
  ;
  ; @return (component)
  ([layout-props]
   [layout (a/id) layout-props])

  ([layout-id layout-props]
   (let [] ;layout-props (a/prot layout-props layout-props-prototype)
        [layout-b layout-id layout-props])))
