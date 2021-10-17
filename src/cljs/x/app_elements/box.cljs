
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.26
; Description:
; Version: v0.3.8
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.box
    (:require [mid-fruits.candy          :refer [param]]
              [x.app-core.api            :as a :refer [r]]
              [x.app-elements.card       :as card :refer [card]]
              [x.app-elements.engine.api :as engine]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- box-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) box-props
  ;
  ; @return (map)
  ;  {:color (keyword)
  ;   :min-width (keyword)}
  [box-props]
  (card/card-props-prototype (merge {:color     :default
                                     :min-width :m}
                                    (param box-props))))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) box-id
  ;
  ; @return (map)
  [db [_ box-id]]
  (r engine/get-element-view-props db box-id))

(a/reg-sub ::get-view-props get-view-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; XXX#3240
  ; A box elem alapkomponense a card elem.
  ; A box elem további paraméterezését a card elem dokumentációjában találod.
  ;
  ; @param (keyword)(opt) box-id
  ; @param (map) box-props
  ;  {:border-color (keyword)(opt)
  ;    :primary, :secondary, :warning, :success, :muted, :default
  ;    Default: :default
  ;   :expandable? (boolean)(opt)
  ;    Default: false
  ;   :expanded? (boolean)(opt)
  ;    Default: false
  ;    Only w/ {:expandable? true}
  ;   :min-width (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl, :none
  ;    Default: :m}
  ;
  ; @usage
  ;  [elements/box {...}]
  ;
  ; @usage
  ;  [elements/box :my-box {...}]
  ;
  ; @return (component)
  ([box-props]
   [view nil box-props])

  ([box-id box-props]
   (let [box-id    (a/id   box-id)
         box-props (a/prot box-props box-props-prototype)]
        [engine/container box-id
          {:base-props box-props
           :component  card
           :subscriber [::get-view-props box-id]}])))
