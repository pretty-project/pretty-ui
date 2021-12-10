

(ns playground.crypto
  (:require
    [x.app-core.api :as a]))

;; -----------------------------------------------------------------------------
;; ---- Components ----

(defn page []
  [:div
   [:h2 "Crypto"]])

(defn view [_ {:keys [view-id] :as view-props}]
  [:div
   [:p (str view-id)]])

;; ---- Components ----
;; -----------------------------------------------------------------------------

;; -----------------------------------------------------------------------------
;; ---- Lifecycles ----



(a/reg-event-fx
  :crypto/render!
  [:ui/set-surface! ::view {:view {:content    #'view
                                   :subscriber [:view-selector/get-view-props :crypto]}}])

(a/reg-event-fx
  :crypto/load!
  {:dispatch-n [[:ui/set-header-title! "Crypto"]
                [:ui/set-window-title! "Crypto"]
                [:crypto/render!]]})
