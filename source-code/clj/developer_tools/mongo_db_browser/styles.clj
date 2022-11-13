
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns developer-tools.mongo-db-browser.styles)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-bar-style
  []
  (str "display: flex; background-color: #d5c1ef; position: absolute; top: 0; left: 0"))

(defn menu-button-style
  [& [{:keys [warning?]}]]
  (str "text-decoration: none; padding: 0 16px; line-height: 32px; display: flex;"
       "border-radius: 16px; margin: 8px; color: #222 !important; "
       "font-size: 13px; font-weight: 500; border: 1px solid #888; cursor: pointer;"
       "background: " (if warning? "#f3a9e3" "#cbf1dc")));)))


(defn remove-button-style
  []
  (str "color: #111 !important; text-decoration: none; background-color: #ff98f2; border-radius: 12px;"
       "line-height: 24px; display: flex;"
       "padding: 0 16px; font-size: 12px"))

(defn collection-button-style
  []
  (str "color: #222 !important; padding: 8px; display: block; margin: 4px; background: #d0d0d0;"
       "text-decoration: none; width: 360px; border-radius: 4px"))

(defn document-style
  [& [{:keys [document-dex]}]]
  (str "padding: 16px; color: #333;"
       "background: " (if (even? document-dex) "#fafafa" "#f0f0f0")))
