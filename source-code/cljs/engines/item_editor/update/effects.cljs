
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-editor.update.effects
    (:require [engines.item-editor.core.subs         :as core.subs]
              [engines.item-editor.update.queries    :as update.queries]
              [engines.item-editor.update.validators :as update.validators]
              [re-frame.api                          :as r :refer [r]]
              [x.ui.api                              :as x.ui]))



;; -- Save item effects -------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-editor/save-item!
  ; @param (keyword) editor-id
  ; @param (map) action-props
  ; {:on-failure (metamorphic-event)(opt)
  ;  :on-success (metamorphic-event)(opt)}
  ;
  ; @usage
  ; [:item-editor/save-item! :my-editor]
  (fn [{:keys [db]} [_ editor-id {:keys [on-failure on-success]}]]
      (let [query        (r update.queries/get-save-item-query          db editor-id)
            validator-f #(r update.validators/save-item-response-valid? db editor-id %)]
           {:db       (r x.ui/fake-process! db 15)
            :dispatch [:pathom/send-query! (r core.subs/get-request-id db editor-id)
                                           {:display-progress? false
                                            :on-success        on-success
                                            :on-failure        on-failure
                                            :query             query
                                            :validator-f       validator-f}]})))
