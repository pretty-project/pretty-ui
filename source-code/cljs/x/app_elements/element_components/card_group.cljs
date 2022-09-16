
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.element-components.card-group
    (:require [mid-fruits.candy          :refer [param]]
              [mid-fruits.random         :as random]
              [mid-fruits.vector         :as vector]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a :refer [r]]
              [x.app-elements.card.views :as card.views]
              [x.app-elements.engine.api :as engine]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- group-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) group-props
  ;
  ; @return (map)
  ;  {:horizontal-align (keyword)}
  [group-props]
  (merge {:horizontal-align :center}
         (param group-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- card-group
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;  {:cards (maps in vector)}
  [group-id {:keys [cards] :as group-props}]
  (letfn [(f [card-list card-props] (conj card-list [card.views/element card-props]))]
         [:div.x-card-group (engine/element-attributes group-id group-props)
                            (reduce f [:<>] cards)]))

(defn element
  ; XXX#3240
  ; A card-group elem számára :cards tulajdonságként átadott vektor a card-group elemen megjelenített
  ; card elemek paraméter térképeit tartalmazza.
  ; A card elemek paraméterézének leírását a card elem dokumentációjában találod.
  ;
  ; @param (keyword)(opt) group-id
  ; @param (map) group-props
  ;  {:cards (maps in vector)
  ;    [{...} {...}]
  ;   :class (keyword or keywords in vector)(opt)
  ;   :horizontal-align (keyword)(opt)
  ;    :center, :left, :right
  ;    Default: :center
  ;   :indent (map)(opt)
  ;    {:bottom (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :left (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :right (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :top (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl}
  ;   :style (map)(opt)}
  ;
  ; @usage
  ;  [elements/card-group {...}]
  ;
  ; @usage
  ;  [elements/card-group :my-card-group {...}]
  ([group-props]
   [element (random/generate-keyword) group-props])

  ([group-id group-props]
   (let [group-props (group-props-prototype group-props)]
        [card-group group-id group-props])))
