
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.21
; Description:
; Version: v0.1.8
; Compatibility: x4.4.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-tools.api
    (:require [x.app-tools.scheduler]
              [x.app-tools.file-saver]
              [x.app-tools.clipboard           :as clipboard]
              [x.app-tools.image-loader        :as image-loader]
              [x.app-tools.image-preloader     :as image-preloader]
              [x.app-tools.infinite-loader     :as infinite-loader]
              [x.app-tools.temporary-component :as temporary-component]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-tools.clipboard
(def copy-to-clipboard! clipboard/copy-to-clipboard!)

; x.app-tools.image-loader
(def image-loader image-loader/component)

; x.app-tools.image-preloader
(def image-preloader image-preloader/component)

; x.app-tools/infinite-loader
(def pause-infinite-loader!  infinite-loader/pause-infinite-loader!)
(def restart-infinite-loader infinite-loader/restart-infinite-loader!)
(def infinite-loader         infinite-loader/component)

; x.app-tools/temporary-component
(def append-temporary-component! temporary-component/append-temporary-component!)
(def remove-temporary-component! temporary-component/remove-temporary-component!)
