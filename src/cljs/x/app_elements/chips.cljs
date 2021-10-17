
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.07.03
; Description:
; Version: v0.6.2
; Compatibility: x4.3.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.chips
    (:require [mid-fruits.candy          :refer [param]]
              [mid-fruits.loop           :refer [reduce-indexed]]
              [mid-fruits.vector         :as vector]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a :refer [r]]
              [x.app-elements.engine.api :as engine]
              [x.app-elements.chip       :refer [view] :rename {view chip}]))



;; -- Converters --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-props->on-delete
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) chips-id
  ; @param (map) view-props
  ;  {:delete-chip-event (event-vector)(opt)}
  ; @param (map) chip-props
  ; @param (integer) chip-dex
  ;
  ; @return (map)
  ;  {:on-delete (metamorphic-event)}
  [chips-id {:keys [delete-chip-event]} _ chip-dex]
  (vector/concat-items delete-chip-event [chips-id chip-dex]))

(defn- view-props->chip-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) chips-id
  ; @param (map) view-props
  ;  {:delete-chip-event (event-vector)(opt)}
  ; @param (map) chip-props
  ; @param (integer) chip-dex
  ;
  ; @return (map)
  ;  {:on-delete (metamorphic-event)}
  [chips-id {:keys [delete-chip-event] :as view-props} chip-props chip-dex]
  (if (some? delete-chip-event)
      (let [on-delete (view-props->on-delete chips-id view-props chip-props chip-dex)]
           (assoc chip-props :on-delete on-delete))))



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

(defn chips-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) chips-props
  ;
  ; @return (map)
  [chips-props]
  (merge {}
         (param chips-props)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) chips-id
  ;
  ; @return (map)
  [db [_ chips-id]]
  (r engine/get-element-view-props db chips-id))

(a/reg-sub ::get-view-props get-view-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- chips-no-chips-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) chips-id
  ; @param (map) view-props
  ;  {:no-chips-label (metamorphic-content)(opt)}
  ;
  ; @return (hiccup)
  [_ {:keys [no-chips-label]}]
  (if (some? no-chips-label)
      [:div.x-chips--no-chips-label [components/content {:content no-chips-label}]]))

(defn- chips-chips
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) chips-id
  ; @param (map) view-props
  ;  {:chips (* in vector)}
  ;
  ; @return (hiccup)
  [chips-id {:keys [chips] :as view-props}]
  (if (vector/nonempty? chips)
      (reduce-indexed #(let [chip-props (view-props->chip-props chips-id view-props %2 %3)
                             chip-props (a/prot chip-props chip-props-prototype)]
                            (vector/conj-item %1 [chip chip-props]))
                       [:div.x-chips--chips]
                       (param chips))
      [chips-no-chips-label chips-id view-props]))

(defn- chips-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) chips-id
  ; @param (map) view-props
  ;  {:label (metamorphic-content)(opt)}
  ;
  ; @return (hiccup)
  [_ {:keys [label]}]
  (if (some? label)
      [:div.x-chips--label [components/content {:content label}]]))

(defn- chips
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) chips-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [chips-id view-props]
  [:div.x-chips
    (engine/element-attributes chips-id view-props)
    [chips-label chips-id view-props]
    [chips-chips chips-id view-props]])

(defn view
  ;  XXX#7701
  ; A chips elem számára :chips tulajdonságként átadott vektor a chips elemen megjelenített
  ; chip elemek paraméter térképeit tartalmazza.
  ; A chip elemek paraméterézének leírását a chip elem dokumentációjában találod.
  ;
  ; @param (keyword)(opt) chips-id
  ; @param (map) chips-props
  ;  {:chips (maps in vector)
  ;    [{...} {...}]
  ;   :class (string or vector)(opt)
  ;   :delete-chip-event (event-vector)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :label (metamorphic-content)(opt)
  ;   :no-chips-label (metamorphic-content)(opt)
  ;   :style (map)(opt)}
  ;
  ; @usage
  ;  [elements/chips {...}]
  ;
  ; @usage
  ;  [elements/chips :my-chips {...}]
  ;
  ;
  ; @usage
  ;  [elements/chips {:chips [{:label "Chip #1" :variant :outlined}
  ;                           {:label "Chip #2" :variant :filled}]}]
  ;
  ; @usage
  ;  (a/reg-event-db :delete-my-chip! [db [_ my-param chips-id chip-dex]])
  ;  [elements/chips {:chips [{:label "Chip #1" :variant :outlined}
  ;                           {:label "Chip #2" :variant :filled}]
  ;                   :delete-chip-event [:delete-my-chip! :my-param]}]
  ;
  ; @return (component)
  ([chips-props]
   [view nil chips-props])

  ([chips-id chips-props]
   (let [chips-id    (a/id   chips-id)
         chips-props (a/prot chips-props chips-props-prototype)]
        [engine/container chips-id
          {:base-props chips-props
           :component  chips}])))
