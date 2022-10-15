
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns developer-tools.core.effects
    (:require [dom.api      :as dom]
              [re-frame.api :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :developer-tools.core/test!
  ; @usage
  ;  [:developer-tools.core/test!]
  [:ui/render-bubble! {:body "It works!"}])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :developer-tools.core/toggle-design-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (dom/toggle-design-mode!)
      {:db (update-in db [:developer-tools :core/meta-items :design-mode?] not)}))
