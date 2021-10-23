
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.26
; Description:
; Version: v0.4.0
; Compatibility: x4.4.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.card-desk
    (:require [mid-fruits.candy          :refer [param]]
              [mid-fruits.keyword        :as keyword]
              [mid-fruits.vector         :as vector]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a :refer [r]]
              [x.app-elements.card       :refer [view] :rename {view card}]
              [x.app-elements.engine.api :as engine]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- desk-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) desk-props
  ;
  ; @return (map)
  ;  {:horizontal-align (keyword)}
  [{:keys [request-id] :as desk-props}]
  (merge {:horizontal-align :center}
         (param desk-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- card-desk
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) desk-id
  ; @param (map) desk-props
  ;  {:cards (maps in vector)}
  ;
  ; @return (hiccup)
  [desk-id {:keys [cards] :as desk-props}]
  (reduce #(vector/conj-item %1 [card %2])
           [:div.x-card-desk (engine/element-attributes desk-id desk-props)]
           (param cards)))

(defn view
  ; XXX#3240
  ; A card-desk elem számára :cards tulajdonságként átadott vektor a card-desk elemen megjelenített
  ; card elemek paraméter térképeit tartalmazza.
  ; A card elemek paraméterézének leírását a card elem dokumentációjában találod.
  ;
  ; @param (keyword)(opt) desk-id
  ; @param (map) desk-props
  ;  {:cards (maps in vector)
  ;    [{...} {...}]
  ;   :class (string or vector)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :horizontal-align (keyword)(opt)
  ;    :left, :center, :right
  ;    Default: :center
  ;   :style (map)(opt)}
  ;
  ; @usage
  ;  [elements/card-desk {...}]
  ;
  ; @usage
  ;  [elements/card-desk :my-card-desk {...}]
  ;
  ; @return (component)
  ([desk-props]
   [view nil desk-props])

  ([desk-id desk-props]
   (let [desk-id    (a/id desk-id)
         desk-props (a/prot desk-props desk-props-prototype)]
        [card-desk desk-id desk-props])))
