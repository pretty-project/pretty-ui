
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.view-selector.core.subs
    (:require [mid-fruits.candy  :refer [return]]
              [mid-fruits.vector :as vector]
              [x.app-core.api    :as a :refer [r]]
              [x.app-router.api  :as router]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-selector-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (map)
  [db [_ extension-id]]
  (get-in db [extension-id :view-selector/meta-items]))

(defn get-meta-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-key
  ;
  ; @return (*)
  [db [_ extension-id item-key]]
  (get-in db [extension-id :view-selector/meta-items item-key]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-derived-view-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (keyword)
  ;  A get-derived-view-id függvény visszatérési értéke
  ;  1. A :view-id útvonal-paraméter
  ;  2. A {:default-view-id ...} paraméter
  [db [_ extension-id]]
  (let [default-view-id (r get-meta-item db extension-id :default-view-id)]
       (if-let [derived-view-id (r router/get-current-route-path-param db :view-id)]
               (let [derived-view-id  (keyword derived-view-id)
                     allowed-view-ids (r get-meta-item db extension-id :allowed-view-ids)]
                    (if (or (not (vector?          allowed-view-ids))
                            (vector/contains-item? allowed-view-ids derived-view-id))
                        ; If allowed-view-ids is NOT in use,
                        ; or allowed-view-ids is in use & derived-view-id is allowed ...
                        (return derived-view-id)
                        ; If allowed-view-ids is in use & derived-view-id is NOT allowed ...
                        (return default-view-id)))
               (return default-view-id))))

(defn get-selected-view-id
  ; @param (keyword) extension-id
  ;
  ; @usage
  ;  (r view-selector/get-selected-view-id db :my-extension)
  ;
  ; @return (keyword)
  [db [_ extension-id]]
  (r get-meta-item db extension-id :view-id))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:view-selector/get-selected-view-id :my-extension]
(a/reg-sub :view-selector/get-selected-view-id get-selected-view-id)
