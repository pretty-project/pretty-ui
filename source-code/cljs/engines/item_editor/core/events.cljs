
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-editor.core.events
    (:require [candy.api                          :refer [return]]
              [engines.engine-handler.core.events :as core.events]
              [engines.item-editor.backup.events  :as backup.events]
              [engines.item-editor.body.subs      :as body.subs]
              [engines.item-editor.core.subs      :as core.subs]
              [map.api                            :as map :refer [dissoc-in]]
              [re-frame.api                       :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.engine-handler.core.events
(def set-meta-item!     core.events/set-meta-item!)
(def remove-meta-items! core.events/remove-meta-items!)
(def set-mode!          core.events/set-mode!)
(def set-engine-error!  core.events/set-engine-error!)
(def set-item-id!       core.events/set-item-id!)
(def update-item-id!    core.events/update-item-id!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-recovery-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  ; A {:recovery-mode? true} beállítással elindítitott szerkesztő visszaállítja
  ; az elemet az utoljára eltárolt másolat alapján.
  (r set-mode! db editor-id :recovery-mode?))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reset-downloads!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  ; A body komponens component-will-unmount életciklusa által alkalmazott
  ; reset-downloads! függvény nem törli ki az elemről készült backup-item másolatot,
  ; hogy az engine elhagyása utáni esetleges "El nem mentett változtatások visszaállítása"
  ; funkció használatakor a {:recovery-mode? true} állapotban újra elinduló engine
  ; számára elérhetők legyenek a visszaállításhoz szükséges adatok.
  (let [item-path        (r body.subs/get-body-prop db editor-id :item-path)
        suggestions-path (r body.subs/get-body-prop db editor-id :suggestions-path)]
       (-> db (dissoc-in [:engines :engine-handler/meta-items editor-id :data-received?])
              (dissoc-in item-path)
              (dissoc-in suggestions-path))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn use-default-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  ; XXX#5067 (source-code/cljs/engines/item_editor/core/events.cljs)
  (let [default-item (r body.subs/get-body-prop db editor-id :default-item)
        item-path    (r body.subs/get-body-prop db editor-id :item-path)]
       (update-in db item-path map/reversed-merge default-item)))

(defn use-initial-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  (if (r core.subs/new-item? db editor-id)
      (let [initial-item (r body.subs/get-body-prop db editor-id :initial-item)
            item-path    (r body.subs/get-body-prop db editor-id :item-path)]
           (assoc-in db item-path initial-item))
      (return db)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn load-editor!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  (r update-item-id! db editor-id))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :item-editor/set-engine-error! set-engine-error!)
