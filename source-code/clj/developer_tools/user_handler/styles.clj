
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns developer-tools.user-handler.styles)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-bar-style
  []
  (str "display: flex; background-color: #d5c1ef; position: absolute; top: 0; left: 0"))

(defn menu-button-style
  [& [{:keys [warning?]}]]
  (str "text-decoration: none; padding: 0 16px; line-height: 32px; display: flex;"
       "border-radius: 16px; margin: 8px; color: #222 !important; "
       "font-size: 13px; font-weight: 500; border: 1px solid #888;"
       "background: " (if warning? "#f3a9e3" "#cbf1dc")));)))

(defn form-style
  []
  (str "display: flex; justify-content: flex-start; flex-direction: column;"
       "align-items: center; padding-top: 48px"))

(defn field-style
  []
  (str "width: 300px;"))
