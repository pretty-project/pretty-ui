
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.x.db.backup-handler.effects
    (:require [mid.x.db.backup-handler.subs :as backup-handler.subs]
              [re-frame.api                 :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :db/resolve-backup-item!
  ; @param (vector) item-path
  ; @param (map) events
  ;  {:on-changed (metamorphic-event)(opt)
  ;   :on-unchanged (metamorphic-event)(opt)}
  (fn [{:keys [db]} [_ item-path {:keys [on-changed on-unchanged]}]]
      (if (r backup-handler.subs/item-changed? db item-path)
          {:dispatch on-changed}
          {:dispatch on-unchanged})))
