
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.file-editor.update.effects
    (:require [engines.file-editor.backup.events     :as backup.events]
              [engines.file-editor.body.subs         :as body.subs]
              [engines.file-editor.core.subs         :as core.subs]
              [engines.file-editor.update.queries    :as update.queries]
              [engines.file-editor.update.validators :as update.validators]
              [re-frame.api                          :as r :refer [r]]))



;; -- Save content effects ----------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :file-editor/save-content!
  ; @param (keyword) editor-id
  ; @param (map) action-props
  ; {:on-failure (metamorphic-event)(opt)
  ;  :on-success (metamorphic-event)(opt)}
  ;
  ; @usage
  ; [:file-editor/save-content! :my-editor]
  (fn [{:keys [db]} [_ editor-id {:keys [on-failure on-success]}]]
      (let [display-progress? (r body.subs/get-body-prop                        db editor-id :display-progress?)
            query             (r update.queries/get-save-content-query          db editor-id)
            validator-f      #(r update.validators/save-content-response-valid? db editor-id %)]
           [:pathom/send-query! (r core.subs/get-request-id db editor-id)
                                {:display-progress? display-progress?
                                 :on-success        {:dispatch-n [on-success [:file-editor/backup-saved-content! editor-id]]}
                                 :on-failure        on-failure
                                 :query             query
                                 :validator-f       validator-f}])))

(r/reg-event-fx :file-editor/backup-saved-content
  ; @param (keyword) editor-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ editor-id _]]
      ; A file-editor engine a tartalom mentésének befejeződésekor ...
      ; ... az item-editor engine-nel ellentétben nem lép ki a szerkesztőből,
      ;     ezért szükséges a tartalomról tárolt másolatot frissíteni,
      ;     hogy a backup.subs/form-changed? és a backup.subs/content-changed?
      ;     függvények kimentei visszaálljanak alaphelyzetbe.
      ; ... megtörténik a body komponens számára esetlegesen átadott on-saved esemény.
      (r backup.events/backup-current-content! db editor-id)))
