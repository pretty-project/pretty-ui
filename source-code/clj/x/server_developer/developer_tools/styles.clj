
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-developer.developer-tools.styles)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-style
  []
  "padding: 0; margin: 0")

(defn menu-bar-style
  []
  (str "display: flex; width: 100%; background-color: #d5c1ef; justify-content: flex-end;"))

(defn menu-item-style
  []
  (str "text-decoration: none; padding: 0 16px; line-height: 32px; display: block;"
       "background-color: #cbf1dc; margin: 8px; border-radius: 16px; "
       "font-weight: 500; font-size: 13px; color: #222 !important;"
       " border: 1px solid #888"))
