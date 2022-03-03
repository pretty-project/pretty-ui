
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.components.chip-group
    (:require [mid-fruits.candy               :refer [param return]]
              [mid-fruits.loop                :refer [reduce-indexed]]
              [mid-fruits.vector              :as vector]
              [x.app-components.api           :as components]
              [x.app-core.api                 :as a :refer [r]]
              [x.app-elements.components.chip :rename {element chip}]
              [x.app-elements.engine.api      :as engine]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- group-props->chip-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;  {:on-delete (event-vector)(opt)}
  ; @param (map) chip-props
  ; @param (integer) chip-dex
  ;
  ; @return (map)
  ;  {:on-delete (metamorphic-event)}
  [group-id {:keys [on-delete] :as group-props} chip-props chip-dex]
  (if on-delete (let [on-delete (a/metamorphic-event<-params on-delete group-id chip-dex)]
                     (assoc chip-props :on-delete on-delete))
                (return chip-props)))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn chip-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) chip-props
  ;
  ; @return (map)
  ;  {:layout (keyword)}
  [chip-props]
  (merge {:layout :fit}
         (param chip-props)))

(defn group-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) group-props
  ;
  ; @return (map)
  [group-props]
  (merge {}
         (param group-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- chip-group-no-chips-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;  {:no-chips-label (metamorphic-content)(opt)}
  [_ {:keys [no-chips-label]}]
  (if no-chips-label [:div.x-chip-group--no-chips-label [components/content no-chips-label]]))

(defn- chip-group-chips
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;  {:chips (* in vector)}
  [group-id {:keys [chips] :as group-props}]
  (if (vector/nonempty? chips)
      (reduce-indexed #(let [chip-props (group-props->chip-props group-id group-props %3 %2)
                             chip-props (chip-props-prototype    chip-props)]
                            (conj %1 [chip chip-props]))
                       [:div.x-chip-group--chips]
                       (param chips)))
  [chip-group-no-chips-label group-id group-props])

(defn- chip-group-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;  {:label (metamorphic-content)(opt)}
  [_ {:keys [label]}]
  (if label [:div.x-chip-group--label [components/content label]]))

(defn- chip-group
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  [group-id group-props]
  [:div.x-chip-group (engine/element-attributes group-id group-props)
                     [chip-group-label          group-id group-props]
                     [chip-group-chips          group-id group-props]])

(defn element
  ;  XXX#7701
  ; A chip-group elem számára :chips tulajdonságként átadott vektor a megjelenített
  ; chip elemek paraméter térképeit tartalmazza.
  ; A chip elemek paraméterézének leírását a chip elem dokumentációjában találod.
  ;
  ; @param (keyword)(opt) group-id
  ; @param (map) group-props
  ;  {:chips (maps in vector)
  ;    [{...} {...}]
  ;   :class (keyword or keywords in vector)(opt)
  ;   :indent (keyword)(opt)
  ;    :left, :right, :both, :none
  ;    Default: :none
  ;   :label (metamorphic-content)(opt)
  ;   :no-chips-label (metamorphic-content)(opt)
  ;   :on-delete (event-vector)(opt)
  ;   :style (map)(opt)}
  ;
  ; @usage
  ;  [elements/chip-group {...}]
  ;
  ; @usage
  ;  [elements/chip-group :my-chip-group {...}]
  ;
  ; @usage
  ;  [elements/chip-group {:chip-group [{:label "Chip #1"}]}]
  ;
  ; @usage
  ;  (a/reg-event-db :delete-my-chip! [db [_ my-param group-id chip-dex]])
  ;  [elements/chip-group {:chips [{:label "Chip #1"}
  ;                                {:label "Chip #2"}]
  ;                        :on-delete [:delete-my-chip! :my-param]}]
  ([group-props]
   [element (a/id) group-props])

  ([group-id group-props]
   (let [];group-props (group-props-prototype group-props)
        [chip-group group-id group-props])))
