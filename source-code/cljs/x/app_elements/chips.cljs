
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.07.03
; Description:
; Version: v0.6.8
; Compatibility: x4.4.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.chips
    (:require [mid-fruits.candy          :refer [param return]]
              [mid-fruits.loop           :refer [reduce-indexed]]
              [mid-fruits.vector         :as vector]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a :refer [r]]
              [x.app-elements.engine.api :as engine]
              [x.app-elements.chip       :rename {view chip}]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- chips-props->chip-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) chips-id
  ; @param (map) chips-props
  ;  {:on-delete (event-vector)(opt)}
  ; @param (map) chip-props
  ; @param (integer) chip-dex
  ;
  ; @return (map)
  ;  {:on-delete (metamorphic-event)}
  [chips-id {:keys [on-delete] :as chips-props} chip-props chip-dex]
  (if (some? on-delete)
      (let [on-delete (a/metamorphic-event<-params on-delete chips-id chip-dex)]
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

(defn chips-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) chips-props
  ;
  ; @return (map)
  [chips-props]
  (merge {}
         (param chips-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- chips-no-chips-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) chips-id
  ; @param (map) chips-props
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
  ; @param (map) chips-props
  ;  {:chips (* in vector)}
  ;
  ; @return (hiccup)
  [chips-id {:keys [chips] :as chips-props}]
  (if (vector/nonempty? chips)
      (reduce-indexed #(let [chip-props (chips-props->chip-props chips-id chips-props %2 %3)
                             chip-props (a/prot chip-props chip-props-prototype)]
                            (vector/conj-item %1 [chip chip-props]))
                       [:div.x-chips--chips]
                       (param chips))
      [chips-no-chips-label chips-id chips-props]))

(defn- chips-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) chips-id
  ; @param (map) chips-props
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
  ; @param (map) chips-props
  ;
  ; @return (hiccup)
  [chips-id chips-props]
  [:div.x-chips
    (engine/element-attributes chips-id chips-props)
    [chips-label chips-id chips-props]
    [chips-chips chips-id chips-props]])

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
  ;   :indent (keyword)(opt)
  ;    :left, :right, :both, :none
  ;    Default: :none
  ;   :label (metamorphic-content)(opt)
  ;   :no-chips-label (metamorphic-content)(opt)
  ;   :on-delete (event-vector)(opt)
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
  ;  [elements/chips {:chips [{:label "Chip #1"}]}]
  ;
  ; @usage
  ;  (a/reg-event-db :delete-my-chip! [db [_ my-param chips-id chip-dex]])
  ;  [elements/chips {:chips [{:label "Chip #1"}
  ;                           {:label "Chip #2"}]
  ;                   :on-delete [:delete-my-chip! :my-param]}]
  ;
  ; @return (component)
  ([chips-props]
   [view (a/id) chips-props])

  ([chips-id chips-props]
   (let [];chips-props (a/prot chips-props chips-props-prototype)
        [chips chips-id chips-props])))
