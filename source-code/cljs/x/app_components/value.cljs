
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.06.08
; Description:
; Version: v0.4.2
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-components.value
    (:require [mid-fruits.candy     :refer [param]]
              [mid-fruits.map       :as map]
              [x.app-core.api       :as a :refer [r]]
              [x.app-dictionary.api :as dictionary]))



;; -- Names -------------------------------------------------------------------
;; -- XXX#8711 ----------------------------------------------------------------

; @name value
;  A (metamorphic-value) típust a get-metamorphic-value subscription
;  {:value ...} tulajdonsága valósítja meg.
;  - Értéke lehet egy Re-Frame subscription függvény, amelynek kimenetét
;    kiértékeli metamorphic-value típusként.
;  - Értéke lehet az app-dictionary szótár egy kifejezésére utaló kulcsszó
;    {:value :my-term}
;  - Értéke lehet egy többnyelvő térkép, amely a felhasználói felületen
;    kiválasztott nyelv szerinti kulcshoz tartózó értékként adódik át.
;    {:value {:en [:data :in :english] :hu [:adatok :magyarul]}}
;  - Értéke lehet egy egyszerű string
;    {:value "My value"}
;  - Értéke lehet egy Re-Frame adatbázis útvonal, amelyen talált értéket
;    kiértékeli metamorphic-value típusként.
;
; @name suffix
;  A {:suffix ...} tulajdonságként átadott szöveget toldalékaként használja.



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn extended-props->value-props
  ; @param (map) extended-props
  ;
  ; @return (map)
  [extended-props]
  (map/inherit (param extended-props)
               [:suffix :value]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ab7800
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) value-id
  ; @param (map) value-props
  ;  {:suffix (string)(opt)
  ;   :value (subscriber function, keyword, map, string or item-path vector)}
  ;
  ; @return (string)
  [db [_ value-id {:keys [suffix value]}]]
  (cond (string?  value) (str value suffix)
        (keyword? value) (str (r dictionary/look-up   db value) suffix)
        (map?     value) (str (r dictionary/translate db value) suffix)
        (fn?      value) (let [value (r value db value-id)]
                              (r ab7800 db value-id {:suffix suffix :value value}))
        (vector?  value) (let [value (get-in db value)]
                              (r ab7800 db value-id {:suffix suffix :value value}))))

(defn get-metamorphic-value
  ; @param (keyword)(opt) value-id
  ; @param (map) value-props
  ;  {:suffix (string)(opt)
  ;   :value (subscriber function, keyword, map, string or item-path vector)}
  ;
  ; @usage
  ;  (r components/get-metamorphic-value db {...})
  ;
  ; @usage
  ;  (r components/get-metamorphic-value db :my-value {...})
  ;
  ; @example (subscriber function)
  ;  (defn my-subscription-function [db [_ value-id]] "Return value")
  ;  (r components/get-metamorphic-value db {:value my-subscription-function})
  ;  => "Return value"
  ;
  ; @example (dictionary term as keyword)
  ;  (r components/get-metamorphic-value db {:value :username})
  ;  => "Username"
  ;
  ; @example (multilingual-item as map)
  ;  (r components/get-metamorphic-value db {:value {:en "Window" :hu "Ablak"}})
  ;  => "Window"
  ;
  ; @example (string)
  ;  (r components/get-metamorphic-value db {:value "Hakuna Matata"})
  ;  => "Hakuna Matata"
  ;
  ; @example (item-path vector)
  ;  (def db {:db {:item {:path {:en "Window" :hu "Ablak"}}}})
  ;  (r components/get-metamorphic-value db {:value [:db :item :path]})
  ;  => "Window"
  ;
  ; @return (string)
  [db event-vector]
  (let [value-id    (a/event-vector->second-id   event-vector)
        value-props (a/event-vector->first-props event-vector)]
       (r ab7800 db value-id value-props)))

; @usage
;  [:components/get-metamorphic-value ...]
(a/reg-sub :components/get-metamorphic-value get-metamorphic-value)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn component
  ; @param (keyword)(opt) value-id
  ; @param (map) value-props
  ;  {:suffix (string)(opt)
  ;   :value (metamorphic-value)}
  ;
  ; @usage
  ;  (components/value {...})
  ;
  ; @usage
  ;  (components/value :my-value {...})
  ;
  ; @return (string)
  ([value-props]
   (component (a/id) value-props))

  ([value-id value-props]
   (a/subscribed [:components/get-metamorphic-value value-id value-props])))
