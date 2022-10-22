
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.core.events
    (:require [mid-fruits.candy                   :refer [return]]
              [mid-fruits.map                     :as map :refer [dissoc-in]]
              [plugins.item-editor.backup.events  :as backup.events]
              [plugins.item-editor.body.subs      :as body.subs]
              [plugins.item-editor.core.subs      :as core.subs]
              [plugins.plugin-handler.core.events :as core.events]
              [re-frame.api                       :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.core.events
(def set-meta-item!     core.events/set-meta-item!)
(def remove-meta-items! core.events/remove-meta-items!)
(def set-mode!          core.events/set-mode!)
(def set-item-id!       core.events/set-item-id!)
(def update-item-id!    core.events/update-item-id!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-error-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  (r set-mode! db editor-id :error-mode?))

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
  ; hogy a plugin elhagyása utáni esetleges "El nem mentett változtatások visszaállítása"
  ; funkció használatakor a {:recovery-mode? true} állapotban újra elinduló plugin
  ; számára elérhetők legyenek a visszaállításhoz szükséges adatok.
  (let [item-path        (r body.subs/get-body-prop db editor-id :item-path)
        suggestions-path (r body.subs/get-body-prop db editor-id :suggestions-path)]
       (-> db (dissoc-in [:plugins :plugin-handler/meta-items editor-id :data-received?])
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
  ; XXX#5067 (plugins.item-editor.core.events)
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
(r/reg-event-db :item-editor/set-error-mode! set-error-mode!)
