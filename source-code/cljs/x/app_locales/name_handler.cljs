
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.10.20
; Description:
; Version: v0.4.4
; Compatibility: x4.4.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-locales.name-handler
    (:require [x.app-core.api                 :as a :refer [r]]
              [x.app-locales.language-handler :as language-handler]
              [x.mid-locales.name-handler     :as name-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-locales.name-handler
(def NAME-ORDERS        name-handler/NAME-ORDERS)
(def name->ordered-name name-handler/name->ordered-name)



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-name-order
  ; @usage
  ;  (r locales/get-name-order db)
  ;
  ; @return (keyword)
  ;  :normal, :reversed
  [db _]
  (let [selected-language (r language-handler/get-selected-language db)]
       (get NAME-ORDERS selected-language :normal)))

(defn get-ordered-name
  ; @param (string) first-name
  ; @param (string) last-name
  ;
  ; @usage
  ;  (r locales/get-ordered-name db "My" "Name")
  ;
  ; @return (string)
  [db [_ first-name last-name]]
  (let [name-order (r get-name-order db)]
       (case name-order :reversed (str last-name  " " first-name)
                                  (str first-name " " last-name))))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn name-order
  ; @param (component, hiccup or string) first-name
  ; @param (component, hiccup or string) last-name
  ; @param (keyword) name-order
  ;  :normal, :reversed
  ;
  ; @return (component)
  [first-name last-name name-order]
  (if (= name-order :reversed)
      [:<> last-name  first-name]
      [:<> first-name last-name]))
