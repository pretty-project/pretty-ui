
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.x.locales.name-handler.helpers
    (:require [mid-fruits.string                 :as string]
              [mid.x.locales.name-handler.config :as name-handler.config]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn name->ordered-name
  ; @param (string) first-name
  ; @param (string) last-name
  ; @param (keyword) locale-id
  ;
  ; @usage
  ;  (name->ordered-name "First name" "Last name" :en)
  ;
  ; @return (string)
  [first-name last-name locale-id]
  (let [name-order (get name-handler.config/NAME-ORDERS locale-id)]
       (string/trim (case name-order :reversed (str last-name  " " first-name)
                                               (str first-name " " last-name)))))

(defn name->ordered-initials
  ; @param (string) first-name
  ; @param (string) last-name
  ; @param (keyword) locale-id
  ;
  ; @usage
  ;  (name->ordered-initials "First name" "Last name" :en)
  ;
  ; @return (string)
  [first-name last-name locale-id]
  (let [name-order (get name-handler.config/NAME-ORDERS locale-id)]
       (string/trim (case name-order :reversed (str (first last-name)  (first first-name))
                                               (str (first first-name) (first last-name))))))
