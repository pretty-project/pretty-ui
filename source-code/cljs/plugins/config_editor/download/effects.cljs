
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.config-editor.download.effects
    (:require [plugins.config-editor.body.subs           :as body.subs]
              [plugins.config-editor.core.subs           :as core.subs]
              [plugins.config-editor.download.events     :as download.events]
              [plugins.config-editor.download.queries    :as download.queries]
              [plugins.config-editor.download.validators :as download.validators]
              [x.app-core.api                            :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :config-editor/request-config!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  (fn [{:keys [db]} [_ editor-id]]
      (let [query        (r download.queries/get-request-config-query          db editor-id)
            validator-f #(r download.validators/request-config-response-valid? db editor-id %)]
           {:db       (r download.events/request-config! db editor-id)
            :dispatch [:pathom/send-query! (r core.subs/get-request-id db editor-id)
                                           {:display-progress? true
                                            ; XXX#4057
                                            ; Az on-stalled időzítéssel a UI változásai egyszerre történnek
                                            ; meg a lekérés okozta {:editor-disabled? true} állapot megszűnésével
                                            :on-stalled [:config-editor/receive-config! editor-id]
                                            :on-failure [:config-editor/set-error-mode! editor-id]
                                            :query query :validator-f validator-f}]})))

(a/reg-event-fx
  :config-editor/receive-config!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  (fn [{:keys [db]} [_ editor-id server-response]]
      ; Ha a [:config-editor/receive-config! ...] esemény megtörténésekor a body komponens már
      ; nincs a React-fába csatolva, akkor az esemény nem végez műveletet.
      (if (r body.subs/body-did-mount? db editor-id)
          {:db       (r download.events/receive-config! db editor-id server-response)
           :dispatch [:config-editor/config-received editor-id]})))

(a/reg-event-fx
  :config-editor/config-received
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  (fn [{:keys [db]} [_ editor-id]]))
