
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns developer-tools.re-frame-browser.styles)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-bar-style
  []
  (str "display: flex; background-color: #d5c1ef; position: absolute; top: 0; left: 0"))

(defn menu-button-style
  [& [{:keys [disabled?]}]]
  (str "text-decoration: none; padding: 0 16px; line-height: 32px; display: flex;"
       "border-radius: 16px; margin: 8px; color: #222 !important; "
       "font-size: 13px; font-weight: 500; border: 1px solid #888;"
       "background: #cbf1dc;"
       "opacity: " (if disabled? ".5" "1")))

(defn map-item-style
  []
  (str "color: #222 !important; padding: 8px; display: block; margin: 4px; background: #d0d0d0;"
       "text-decoration: none; width: 360px; border-radius: 4px"))

(defn item-preview-style
  []
  (str "color: #aac; font-size: 12px; line-height: 24px;"))
