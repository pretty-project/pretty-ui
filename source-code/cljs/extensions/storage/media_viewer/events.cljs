
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.storage.media-viewer.events
    (:require [x.app-core.api :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn step-bwd!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ viewer-id]])

(defn step-fwd!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ viewer-id]])

(defn store-directory-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ viewer-id {:media/keys [id] :as directory-item}]]
  (assoc-in db [:storage :media-viewer/data-items id] directory-item))

(defn receive-directory-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ viewer-id server-response]]
  (let [directory-item (get server-response :storage.media-browser/get-item)]
       (r store-directory-item! db viewer-id directory-item)))

(defn load-viewer!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ viewer-id viewer-props]]
  (assoc-in db [:storage :media-viewer/meta-items] viewer-props))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :storage.media-viewer/receive-directory-item! receive-directory-item!)
