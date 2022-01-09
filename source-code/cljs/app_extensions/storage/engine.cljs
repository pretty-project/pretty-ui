
(ns app-extensions.storage.engine
    (:require [x.app-core.api :as a :refer [r]]
              [mid-extensions.storage.engine :as engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-extensions.storage.engine
(def ROOT-DIRECTORY-ID engine/ROOT-DIRECTORY-ID)



;; -- Storage subscriptions ---------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-storage-used-capacity
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (B)
  [db _])
  ;(r get-directory-prop db ROOT-DIRECTORY-ID :directory/content-size))

(defn get-storage-total-capacity
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (B)
  [db _])
  ;(r a/get-storage-detail db :storage-capacity))

(defn get-storage-free-capacity
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (B)
  [db _])
  ;(let [used-capacity  (r get-storage-used-capacity  db)
  ;      total-capacity (r get-storage-total-capacity db)
  ;     (- total-capacity used-capacity)])
