
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-preview.core.effects
    (:require [plugins.item-preview.core.events     :as core.events]
              [plugins.item-preview.core.prototypes :as core.prototypes]
              [re-frame.api                         :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx
  :item-preview/init-preview!
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:collection-name (string)
  ;   :handler-key (keyword)
  ;   :item-namespace (keyword)}
  ;
  ; @usage
  ;  [:item-preview/init-preview! :my-preview {...}]
  (fn [{:keys [db]} [_ preview-id preview-props]]
      (let [preview-props (core.prototypes/preview-props-prototype preview-id preview-props)]
           {:db       (r core.events/init-preview! db preview-id preview-props)
            :dispatch [:item-preview/reg-transfer-preview-props! preview-id preview-props]})))
