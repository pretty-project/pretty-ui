
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.31
; Description:
; Version: v0.3.2
; Compatibility: x4.3.4



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-tools.clipboard
    (:require [app-fruits.dom   :as dom]
              [mid-fruits.candy :refer [param]]
              [x.app-core.api   :as a]
              [x.app-tools.temporary-component
               :refer [append-temporary-component! remove-temporary-component!]]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- copy-to-clipboard!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (?)
  []
  (let [clipboard (dom/get-element-by-id "x-app-clipboard")]
       (js/navigator.clipboard.writeText (.-value clipboard))))



;; -- Events ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :x.app-tools.clipboard/copy-to!
  ; @param (string) text
  ;
  ; @usage
  ;  [:x.app-tools.clipboard/copy-to! "Copy my value to clipboard"]
  (fn [_ [_ text]]
      {:dispatch  [:x.app-ui/blow-bubble! {:content :copied-to-clipboard}]
       ::copy-to! (param text)}))



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



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-fx
  :x.app-tools.clipboard/copy-to!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) text
  (fn [text]
      (append-temporary-component! [clipboard text]
                                   (param copy-to-clipboard!))
      (remove-temporary-component!)))
