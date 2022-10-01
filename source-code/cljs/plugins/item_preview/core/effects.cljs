
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-preview.core.effects
    (:require [plugins.item-preview.core.events :as core.events]
              [plugins.item-preview.core.subs   :as core.subs]
              [re-frame.api                     :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx
  :item-preview/load-preview!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) preview-id
  (fn [{:keys [db]} [_ preview-id]]
      {:db       (r core.events/load-preview! db preview-id)
       :dispatch [:item-preview/request-item! preview-id]}))

(r/reg-event-fx
  :item-preview/reload-preview!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) preview-id
  (fn [{:keys [db]} [_ preview-id]]
      (if (r core.subs/reload-preview? db preview-id)
          {:db       (r core.events/reload-preview! db preview-id)
           :dispatch [:item-preview/request-item! preview-id]})))
