
(ns templates.static-page.api
    (:require [templates.static-page.body.views      :as body.views]
              [templates.static-page.footer.views    :as footer.views]
              [templates.static-page.header.views    :as header.views]
              [templates.static-page.side-menu.views :as side-menu.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; templates.static-page.body.views
(def body body.views/body)

; templates.static-page.footer.views
(def footer footer.views/footer)

; templates.static-page.header.views
(def header header.views/header)

; templates.static-page.side-menu.views
(def side-menu side-menu.views/side-menu)
