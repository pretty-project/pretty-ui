
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.31
; Description: Szöveg másolása a vágólapra
; Version: v0.5.2
; Compatibility: x4.6.1



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-tools.clipboard
    (:require [app-fruits.dom :as dom]
              [x.app-core.api :as a]
              [x.app-tools.temporary-component :refer [append-temporary-component! remove-temporary-component!]]))



;; -- Usage -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  (a/dispatch [:tools/copy-to-clipboard! "My text"])
;
; @usage
;  (reg-event-fx :my-event {:tools/copy-to-clipboard! "My text"})
;
; @usage
;  (ns my-namespace (:require [x.app-tools.api :as tools]))
;  (tools/copy-to-clipboard! "My text")



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- copy-to-clipboard-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [clipboard (dom/get-element-by-id "x-app-clipboard")]
       (js/navigator.clipboard.writeText (.-value clipboard))))


;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- clipboard
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) text
  ;
  ; @return (hiccup)
  [text]
  [:input#x-app-clipboard {:defaultValue text}])



;; -- Events ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :tools/copy-to-clipboard!
  ; @param (string) text
  ;
  ; @usage
  ;  [:tools/copy-to-clipboard! "My text"]
  (fn [_ [_ text]]
      {:dispatch [:ui/blow-bubble! ::notification {:body :copied-to-clipboard}]
       :fx       [:tools/copy-to-clipboard! text]}))



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn copy-to-clipboard!
  ; @usage
  ;  (tools/copy-to-clipboard! "My text")
  ;
  ; @param (string) text
  [text]
  (append-temporary-component! [clipboard text] copy-to-clipboard-f)
  (remove-temporary-component!))

; @usage
;  [:tools/copy-to-clipboard! "My text"]
(a/reg-fx :tools/copy-to-clipboard! copy-to-clipboard!)
