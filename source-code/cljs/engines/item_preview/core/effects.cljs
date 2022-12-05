
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-preview.core.effects
    (:require [engines.item-preview.core.events :as core.events]
              [engines.item-preview.core.subs   :as core.subs]
              [re-frame.api                     :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-preview/load-preview!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) preview-id
  (fn [{:keys [db]} [_ preview-id]]
      {:db       (r core.events/load-preview! db preview-id)
       :dispatch [:item-preview/request-item! preview-id]}))

(r/reg-event-fx :item-preview/reload-preview!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) preview-id
  (fn [{:keys [db]} [_ preview-id]]
      (if (r core.subs/reload-preview? db preview-id)
          {:db       (r core.events/reload-preview! db preview-id)
           :dispatch [:item-preview/request-item! preview-id]})))
