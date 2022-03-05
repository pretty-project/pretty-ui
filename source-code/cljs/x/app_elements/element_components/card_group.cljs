
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.element-components.card-group
    (:require [mid-fruits.candy                       :refer [param]]
              [mid-fruits.vector                      :as vector]
              [x.app-components.api                   :as components]
              [x.app-core.api                         :as a :refer [r]]
              [x.app-elements.element-components.card :rename {element card}]
              [x.app-elements.engine.api              :as engine]))



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
  (reduce #(conj %1 [card %2])
           [:div.x-card-group (engine/element-attributes group-id group-props)
            (param cards)]))

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
  ;    :left, :center, :right
  ;    Default: :center
  ;   :indent (keyword)(opt)
  ;    :left, :right, :both, :none
  ;    Default: :none
  ;   :style (map)(opt)}
  ;
  ; @usage
  ;  [elements/card-group {...}]
  ;
  ; @usage
  ;  [elements/card-group :my-card-group {...}]
  ([group-props]
   [element (a/id) group-props])

  ([group-id group-props]
   (let [group-props (group-props-prototype group-props)]
        [card-group group-id group-props])))