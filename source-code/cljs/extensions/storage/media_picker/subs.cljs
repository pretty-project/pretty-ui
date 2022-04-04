
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.storage.media-picker.subs
    (:require [mid-fruits.candy :refer [return]]
              [x.app-core.api   :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-picked-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;  {:value-path (vector)}
  ;
  ; @return (strings in vector)
  [db [_ _ {:keys [value-path]}]]
  ; - A media-picker feliratkozás függvényei paraméterként fogadják a picker-props térképet,
  ;   így a media-picker komponensnek nem szükséges azt a Re-Frame adatbázisba írnia.
  ;
  ; - XXX#8073
  ;   A {:value-path [...]} tulajdonságként átadott útvonalon a kiválasztott elem(ek)
  ;   string vagy vektor típusként tárolhatók.
  ;   Pl.: ["my-file.png"], "my-file.png"
  (let [picked-items (get-in db value-path)]
       (cond (vector? picked-items) (return picked-items)
             (string? picked-items) [picked-items])))

(defn no-items-picked?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;
  ; @return (boolean)
  [db [_ picker-id picker-props]]
  (let [picked-items (r get-picked-items db picker-id picker-props)]
       (empty? picked-items)))

(defn get-picked-item-count
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;
  ; @return (integer)
  [db [_ picker-id picker-props]]
  (let [picked-items (r get-picked-items db picker-id picker-props)]
       (count picked-items)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :storage.media-picker/get-picked-items get-picked-items)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :storage.media-picker/no-items-picked? no-items-picked?)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :storage.media-picker/get-picked-item-count get-picked-item-count)
